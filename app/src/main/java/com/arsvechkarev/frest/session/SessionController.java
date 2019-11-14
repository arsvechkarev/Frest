package com.arsvechkarev.frest.session;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.arsvechkarev.frest.home.managers.ActivityActionsListener;
import com.arsvechkarev.frest.home.managers.HomeFragmentPresenter;
import com.arsvechkarev.frest.home.views.HomeFragment;
import com.arsvechkarev.frest.models.main.Project;
import com.arsvechkarev.frest.models.main.Task;
import org.greenrobot.eventbus.EventBus;

/**
 * Class which controls all current session elements
 *
 * @author Arseniy Svechkarev
 * @see HomeFragment
 * @see Stopwatch
 * @see TimerService
 */
public class SessionController {

  /**
   * Listener for session actions, like changing timer state, updating time etc.
   */
  private final SessionActionsListener sessionActionsListener;

  /**
   * Intent for starting {@link TimerService}
   */
  private final Intent serviceIntent;

  /**
   * Listener for activity actions related to managing service
   */
  private final ActivityActionsListener activityActionsListener;

  /**
   * Main stopwatch
   */
  private final Stopwatch stopwatch;

  public SessionController(Context mActivityContext,
      SessionActionsListener sessionActionsListener,
      ActivityActionsListener activityActionsListener) {
    this.sessionActionsListener = sessionActionsListener;
    this.activityActionsListener = activityActionsListener;
    serviceIntent = new Intent(mActivityContext, TimerService.class);
    stopwatch = Stopwatch.getInstance();
    stopwatch.setTimeTickListener(this::onTimeTick);
    checkActiveProject();
  }

  private void checkActiveProject() {
    if (stopwatch.getState() != TimerState.INACTIVE) {
      TimerState state = stopwatch.getState();
      Project project = stopwatch.getCurrentProject();
      long currentTime = stopwatch.getTime();

      activityActionsListener.startSessionService(serviceIntent);
      CurrentSessionModel model = new CurrentSessionModel(project, state, currentTime);
      EventBus.getDefault().post(model);
    }
  }

  /**
   * Invokes from {@link HomeFragmentPresenter} when user starts new session
   */
  public void startSession(Project project) {
    stopwatch.setCurrentProject(project);
    stopwatch.start();
    activityActionsListener.startSessionService(serviceIntent);
  }

  /**
   * Invokes when user pauses/resumes current session
   */
  public void pauseResumeSession() {
    if (stopwatch.getState() == TimerState.ACTIVE) {
      stopwatch.pause();
    } else {
      stopwatch.resume();
    }
    sessionActionsListener.onSessionStateChanged(stopwatch.getState());
  }

  /**
   * When user want to stop session and save result
   */
  public void stopSession() {
    Project project = stopwatch.getCurrentProject();
    long duration = stopwatch.getTime();
    duration = (duration > 0) ? duration : 1;
    assert project != null; // project not null, because stopwatch doesn't reset yet
    Task newTask = new Task(project, duration);
    sessionActionsListener.onSessionFinished(newTask);
    makeStop();
  }

  /**
   * When user wants to cancel session and so not save result
   */
  public void cancelSession() {
    makeStop();
    sessionActionsListener.onSessionCancelled();
  }

  public boolean isSessionInactive() {
    return stopwatch.getState() == TimerState.INACTIVE;
  }

  @Nullable
  public Project getCurrentProject() {
    return stopwatch.getCurrentProject();
  }

  private void makeStop() {
    stopwatch.stop();
    stopwatch.reset();
    activityActionsListener.stopSessionService(serviceIntent);
  }

  /**
   * Method which posts information about changing time to {@link TimerService} and {@link
   * HomeFragmentPresenter}
   *
   * @param time Time from stopwatch (in seconds)
   */
  private void onTimeTick(long time) {
    Duration duration = new Duration(time);
    EventBus.getDefault().post(duration);
  }
}
