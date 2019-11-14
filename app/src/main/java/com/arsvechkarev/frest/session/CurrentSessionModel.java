package com.arsvechkarev.frest.session;

import com.arsvechkarev.frest.home.views.HomeFragment;
import com.arsvechkarev.frest.models.main.Project;

/**
 * This class is entity for sending current state of current session timer with EventBus
 *
 * @author Arseniy Svechkarev
 * @see HomeFragment
 * @see SessionController
 */
public class CurrentSessionModel {

  /**
   * Current project type
   */
  private final Project project;

  /**
   * State of timer
   */
  private final TimerState state;

  /**
   * Current time in seconds
   */
  private final long currentTime;

  CurrentSessionModel(Project project, TimerState state, long currentTime) {
    this.project = project;
    this.state = state;
    this.currentTime = currentTime;
  }

  public Project getProject() {
    return project;
  }

  public TimerState getState() {
    return state;
  }

  public long getCurrentTime() {
    return currentTime;
  }
}
