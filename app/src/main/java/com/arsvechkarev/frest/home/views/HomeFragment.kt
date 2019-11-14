package com.arsvechkarev.frest.home.views

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.R.layout
import com.arsvechkarev.frest.additional.Codes.Key
import com.arsvechkarev.frest.additional.datetime.toStandardTime
import com.arsvechkarev.frest.home.managers.ActivityActionsListener
import com.arsvechkarev.frest.home.managers.CurrentSessionListener
import com.arsvechkarev.frest.home.managers.HomeFragmentPresenter
import com.arsvechkarev.frest.home.managers.LayoutActionsListener
import com.arsvechkarev.frest.home.managers.TimelineActionsListener
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import com.arsvechkarev.frest.reports.byproject.SpecProjectReportsActivity
import com.arsvechkarev.frest.session.CurrentSessionModel
import com.arsvechkarev.frest.session.TimerState
import com.arsvechkarev.frest.timeline.TimelineAdapter
import com.arsvechkarev.frest.timeline.TimelineDiffCallback
import kotlinx.android.synthetic.main.fragment_home.cardNewTask
import kotlinx.android.synthetic.main.fragment_home.currentSessionLayout
import kotlinx.android.synthetic.main.fragment_home.noTasksStub
import kotlinx.android.synthetic.main.fragment_home.recyclerTimeline
import kotlinx.android.synthetic.main.item_current_session.imageCancel
import kotlinx.android.synthetic.main.item_current_session.imagePauseResume
import kotlinx.android.synthetic.main.item_current_session.imageStop
import kotlinx.android.synthetic.main.item_current_session.imgPauseResumeSwitch
import kotlinx.android.synthetic.main.item_current_session.layoutCurrentSessionRect
import kotlinx.android.synthetic.main.item_current_session.textProjectName
import kotlinx.android.synthetic.main.item_current_session.timeCurrentSession
import kotlinx.android.synthetic.main.stub_template.stubDescription
import kotlinx.android.synthetic.main.stub_template.stubImage
import kotlinx.android.synthetic.main.stub_template.stubTitle
import java.util.ArrayList

/**
 * Base Fragment with all tasks timeline.
 *
 * @author Arseniy Svechkarev
 * @see MainActivity
 * @see HomeFragmentPresenter
 */
class HomeFragment : androidx.fragment.app.Fragment(), TimelineActionsListener,
  CurrentSessionListener,
  LayoutActionsListener {
  
  // Drawables for changing images when user clicks pause/resume on
  private lateinit var drawableIconPlay: Drawable
  private lateinit var drawableIconPause: Drawable
  
  /** For ButterKnife */
  private lateinit var unbinder: Unbinder
  
  /** Context for presenter */
  private lateinit var myContext: Context
  
  /** Presenter */
  private lateinit var presenter: HomeFragmentPresenter
  
  /** Adapter for recycler */
  private lateinit var timelineAdapter: TimelineAdapter
  
  /** Manager for recycler */
  private var layoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
  
  /** Listener for activity actions */
  private lateinit var activityActionsListener: ActivityActionsListener
  
  override fun onAttach(context: Context?) {
    super.onAttach(context)
    myContext = activity as Context
    if (myContext is ActivityActionsListener) {
      activityActionsListener = myContext as ActivityActionsListener
    }
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(layout.fragment_home, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = ButterKnife.bind(this, view)
    initPausePlayDrawables()
    presenter = HomeFragmentPresenter(myContext, this,
      this, activityActionsListener, this)
    presenter.attachView()
    prepareNoDataStub()
    initClickListeners()
  }
  
  override fun onResume() {
    super.onResume()
    presenter.onResumeFragment()
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    unbinder.unbind()
    presenter.detachView()
  }
  
  /** @see HomeFragmentPresenter.attachView */
  override fun prepareRecycler(adapter: TimelineAdapter) {
    timelineAdapter = adapter
    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(myContext)
    layoutManager?.reverseLayout = true
    layoutManager?.stackFromEnd = true
    recyclerTimeline.layoutManager = layoutManager
    recyclerTimeline.adapter = adapter
  }
  
  override fun showNoTasksLayout() {
    noTasksStub.visibility = VISIBLE
    recyclerTimeline.visibility = GONE
  }
  
  /**
   * If [layoutManager] isn't null, then recycler is prepared and we can scroll recycler
   * to top and prepare current session layout. Otherwise, if it is null, then it means
   * that user has not track any project and recycler is empty. In this case we should
   * only prepare layout
   */
  override fun onCurrentSessionStarted(project: Project) {
    if (layoutManager != null) {
      prepareWithScrolling(project)
    } else {
      prepareCurrentSessionLayout(project)
    }
  }
  
  /** @see HomeFragmentPresenter.onCheckingTimerState */
  override fun onCheckingTimerState(sessionModel: CurrentSessionModel) {
    prepareCurrentSessionLayout(sessionModel.project)
    timeCurrentSession.text = sessionModel.currentTime.toStandardTime()
    setPausedOrResumedImage(sessionModel.state)
  }
  
  /** @see HomeFragmentPresenter.onCurrentTimeEvent */
  override fun onTimeCounting(formattedTime: String) {
    timeCurrentSession.text = formattedTime
  }
  
  /** @see HomeFragmentPresenter.startSession */
  fun startSession(project: Project) {
    presenter.startSession(project)
    if (layoutManager != null) {
      prepareWithScrolling(project)
    } else {
      prepareCurrentSessionLayout(project)
    }
  }
  
  /** @see HomeFragmentPresenter.onSessionStateChanged */
  override fun onCurrentSessionStateChanged(state: TimerState) {
    setPausedOrResumedImage(state)
  }
  
  /** Invokes from presenter when session finished of cancelled */
  override fun onCurrentSessionEnded() {
    currentSessionLayout.visibility = GONE
  }
  
  fun manualAddTask(task: Task) {
    presenter.makeManualSavingTask(task)
  }
  
  /** When user wants to go to task statistics */
  override fun onTaskClick(task: Task) {
    val intent = Intent(myContext, SpecProjectReportsActivity::class.java)
    val taskAsProject = Project(task.name, task.color)
    intent.putExtra(Key.PROJECT, taskAsProject as Parcelable)
    startActivity(intent)
  }
  
  override fun onTaskLongClick(task: Task) {
    presenter.onAttemptToRemoveTask(task)
  }
  
  /** When user wants to continue already saved task */
  override fun onContinueTaskClick(task: Task) {
    presenter.continueTaskRequest(task)
  }
  
  /** Updating recyclerTimeline when new task added or removed */
  override fun updateRecycler(currentList: ArrayList<Any>) {
    noTasksStub.visibility = GONE
    recyclerTimeline.visibility = VISIBLE
    val previousList = timelineAdapter.data
    val diffCallback = TimelineDiffCallback(previousList,
      currentList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)
    timelineAdapter.data = currentList
    diffResult.dispatchUpdatesTo(timelineAdapter)
  }
  
  private fun prepareNoDataStub() {
    stubImage.setImageDrawable(ContextCompat.getDrawable(myContext,
      R.drawable.no_tasks_image))
    stubTitle.text = getString(R.string.text_no_tasks_title)
    stubDescription.text = getString(R.string.text_no_tasks_description)
  }
  
  private fun initClickListeners() {
    imagePauseResume.setOnClickListener { presenter.pauseResumeClick() }
    imageStop.setOnClickListener { presenter.stopSession() }
    imageCancel.setOnClickListener { presenter.cancelSession() }
    cardNewTask.setOnClickListener { presenter.startNewTaskRequest() }
  }
  
  private fun initPausePlayDrawables() {
    drawableIconPlay = ContextCompat.getDrawable(myContext, R.drawable.ic_play)!!
    drawableIconPause = ContextCompat.getDrawable(myContext, R.drawable.ic_pause)!!
  }
  
  /** Preparing all components for current session */
  private fun prepareCurrentSessionLayout(project: Project) {
    currentSessionLayout.visibility = VISIBLE
    noTasksStub.visibility = GONE
    imgPauseResumeSwitch.setImageDrawable(drawableIconPause)
    val drawable = layoutCurrentSessionRect.background as GradientDrawable
    val strokeSize = resources.getDimension(R.dimen.stroke_size)
    drawable.setStroke(strokeSize.toInt(), project.color)
    textProjectName.text = project.name
    textProjectName.setTextColor(project.color)
    imagePauseResume.setColorFilter(project.color)
    imageStop.setColorFilter(project.color)
    imageCancel.setColorFilter(project.color)
  }
  
  /**
   * Preparing all components for current session after scrolling recycler to top
   *
   * @see [prepareCurrentSessionLayout]
   */
  private fun prepareWithScrolling(project: Project) {
    val smoothScroller = object :
      androidx.recyclerview.widget.LinearSmoothScroller(myContext) {
      override fun onStop() {
        super.onStop()
        // Scrolling completed, so now we can start preparing layout.
        // Otherwise, there can be a nasty lag :(
        prepareCurrentSessionLayout(project)
      }
    }
    smoothScroller.targetPosition = recyclerTimeline.bottom
    layoutManager?.startSmoothScroll(smoothScroller)
  }
  
  private fun setPausedOrResumedImage(state: TimerState) {
    if (state == TimerState.PAUSED) {
      imgPauseResumeSwitch.setImageDrawable(drawableIconPlay)
    } else if (state == TimerState.ACTIVE) {
      imgPauseResumeSwitch.setImageDrawable(drawableIconPause)
    }
  }
}
