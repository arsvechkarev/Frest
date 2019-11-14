package com.arsvechkarev.frest.home.managers

import android.content.Context
import androidx.annotation.Keep
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.datetime.toStandardTime
import com.arsvechkarev.frest.additional.utils.inBackground
import com.arsvechkarev.frest.additional.utils.showNormalToast
import com.arsvechkarev.frest.home.views.HomeFragment
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import com.arsvechkarev.frest.reports.database.DataWriter
import com.arsvechkarev.frest.session.CancelSessionDialog
import com.arsvechkarev.frest.session.CancelSessionDialog.CancelSessionListener
import com.arsvechkarev.frest.session.CurrentSessionModel
import com.arsvechkarev.frest.session.Duration
import com.arsvechkarev.frest.session.SessionActionsListener
import com.arsvechkarev.frest.session.SessionController
import com.arsvechkarev.frest.session.TimerState
import com.arsvechkarev.frest.starttask.StartTaskActivity
import com.arsvechkarev.frest.timeline.DeleteTaskDialog
import com.arsvechkarev.frest.timeline.TimelineAdapter
import com.arsvechkarev.frest.timeline.TimelineManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Presenter for [HomeFragment]
 *
 * @author Arseniy Svechkarev
 */
class HomeFragmentPresenter(
  private val context: Context,
  
  /** Listener for sending current session events */
  private val sessionListener: CurrentSessionListener,
  
  /** Listener for tasks timeline events */
  private val timelineActionsListener: TimelineActionsListener,
  
  /** Listener for sending standard callbacks to activity */
  private val activityActionsListener: ActivityActionsListener,
  
  /** General listener for callbacks with timeline recycler view */
  private val layoutActionsListener: LayoutActionsListener
) : SessionActionsListener, CancelSessionListener {
  
  /** Controller instance for manipulating with current session */
  private lateinit var sessionController: SessionController
  
  /** Manager for manipulating with tasks list */
  private lateinit var timelineManager: TimelineManager
  
  /** Main recycler adapter */
  private lateinit var timelineAdapter: TimelineAdapter
  
  /** Data writer for saving task to database */
  private lateinit var dataWriter: DataWriter
  
  
  /** Initializing fields, registering EventBus etc. */
  fun attachView() {
    EventBus.getDefault().register(this)
    sessionController = SessionController(context,
      this, activityActionsListener)
    timelineManager = TimelineManager(context)
    timelineAdapter = TimelineAdapter(context,
      timelineActionsListener, timelineManager.timelineList)
    dataWriter = DataWriter(context)
    if (timelineManager.timelineList != null) {
      layoutActionsListener.prepareRecycler(timelineAdapter)
    } else if (sessionController.isSessionInactive) {
      layoutActionsListener.showNoTasksLayout()
    }
  }
  
  /** Invokes when fragment onResume */
  fun onResumeFragment() {
    timelineManager.removeRedundantProjects()
    makeRecyclerUpdate()
  }
  
  /** Subscribe event when connection to service occurs */
  @Subscribe
  @Keep
  fun onCheckingTimerState(model: CurrentSessionModel) {
    sessionListener.onCheckingTimerState(model)
  }
  
  /**
   * Subscribe event when session time updates
   *
   * @param duration Current time in seconds
   */
  @Subscribe
  @Keep
  fun onCurrentTimeEvent(duration: Duration) {
    val formattedTime = duration.seconds.toStandardTime()
    sessionListener.onTimeCounting(formattedTime)
  }
  
  /** @see SessionController.startSession */
  fun startSession(project: Project) {
    sessionController.startSession(project)
  }
  
  /** @see SessionController.pauseResumeSession */
  fun pauseResumeClick() {
    sessionController.pauseResumeSession()
  }
  
  /** @see SessionController.stopSession */
  fun stopSession() {
    sessionController.stopSession()
  }
  
  /** @see SessionController.cancelSession */
  fun cancelSession() {
    CancelSessionDialog(context, this, CancelSessionDialog.CANCEL,
      sessionController.currentProject).show()
  }
  
  /** Invokes when timer pauses, resumes, stops etc. */
  override fun onSessionStateChanged(state: TimerState) {
    sessionListener.onCurrentSessionStateChanged(state)
  }
  
  /** @see SessionController.stopSession */
  override fun onSessionFinished(task: Task) {
    inBackground { dataWriter.saveTask(task) }
    timelineManager.saveTask(task)
    sessionListener.onCurrentSessionEnded()
    makeRecyclerUpdate()
  }
  
  /** @see SessionController.cancelSession */
  override fun onSessionCancelled() {
    sessionListener.onCurrentSessionEnded()
  }
  
  /** When user wants to go to [StartTaskActivity] */
  fun startNewTaskRequest() {
    if (sessionController.isSessionInactive) {
      activityActionsListener.startActivityForResult()
    } else {
      CancelSessionDialog(context, this,
        CancelSessionDialog.CANCEL_AND_START_NEW, sessionController.currentProject).show()
    }
  }
  
  /** When user adds task manually */
  fun makeManualSavingTask(task: Task) {
    inBackground { dataWriter.saveTask(task) }
    timelineManager.saveTask(task)
    makeRecyclerUpdate()
  }
  
  /**
   * When user want to continue task directly from timeline
   *
   * @param task Task, which user selected
   */
  fun continueTaskRequest(task: Task) {
    if (sessionController.isSessionInactive) {
      sessionController.startSession(task)
      sessionListener.onCurrentSessionStarted(task)
    } else {
      if (sessionController.currentProject!!.name != task.name) {
        CancelSessionDialog(context, this, task,
          sessionController.currentProject).show()
      } else {
        showNormalToast(context, context.getString(R.string.text_project_already_running))
      }
    }
  }
  
  /** When user wants to cancel current session or cancel and start new */
  override fun onCancelSessionSelected(actionType: Int) {
    sessionController.cancelSession()
    if (actionType == CancelSessionDialog.CANCEL_AND_START_NEW) {
      activityActionsListener.startActivityForResult()
    }
  }
  
  /** When user wants to cancel current session and continue another task */
  override fun onCancelSessionSelected(task: Task) {
    sessionController.cancelSession()
    sessionController.startSession(task)
    sessionListener.onCurrentSessionStarted(task)
  }
  
  /** When user make long click on particular task */
  fun onAttemptToRemoveTask(task: Task) {
    val dialog = DeleteTaskDialog(context, task,
      DeleteTaskDialog.DeleteTaskListener { this.removeTask(it) })
    dialog.show()
  }
  
  fun detachView() {
    EventBus.getDefault().unregister(this)
  }
  
  private fun makeRecyclerUpdate() {
    if (timelineManager.timelineList != null) {
      if (timelineManager.timelineList!!.isEmpty()) {
        makeStandardUpdate()
        notifyNoTaskLayout()
      } else {
        if (timelineAdapter.data != null) {
          makeStandardUpdate()
        } else {
          layoutActionsListener.prepareRecycler(timelineAdapter)
          makeStandardUpdate()
        }
      }
    } else {
      notifyNoTaskLayout()
    }
  }
  
  private fun makeStandardUpdate() {
    layoutActionsListener.updateRecycler(timelineManager.timelineList)
  }
  
  private fun notifyNoTaskLayout() {
    if (sessionController.isSessionInactive) {
      layoutActionsListener.showNoTasksLayout()
    }
  }
  
  // Removing task and updating recyclerTimeline
  private fun removeTask(task: Task) {
    inBackground { dataWriter.removeTask(task) }
    timelineManager.removeTask(task)
    makeRecyclerUpdate()
  }
}
