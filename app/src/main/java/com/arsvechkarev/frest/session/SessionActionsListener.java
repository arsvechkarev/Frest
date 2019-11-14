package com.arsvechkarev.frest.session;

import com.arsvechkarev.frest.models.main.Task;

/**
 * Actions listener for {@link SessionController}
 */
public interface SessionActionsListener {

  /**
   * When user makes pauses/resumes/stops session
   */
  void onSessionStateChanged(TimerState state);

  /**
   * When session is finished and task is created
   */
  void onSessionFinished(Task task);

  /**
   * When user cancels current session and don't saves result
   */
  void onSessionCancelled();
}