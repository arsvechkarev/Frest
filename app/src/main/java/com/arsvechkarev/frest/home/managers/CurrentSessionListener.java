package com.arsvechkarev.frest.home.managers;

import com.arsvechkarev.frest.additional.datetime.TimeUtils;
import com.arsvechkarev.frest.models.main.Project;
import com.arsvechkarev.frest.session.CurrentSessionModel;
import com.arsvechkarev.frest.session.TimerState;

/**
 * Interface for sending callbacks in current session
 *
 * @author Arseniy Svechkarev
 */
public interface CurrentSessionListener {

  /**
   * When user starts new session
   *
   * @param project Project which user chose
   */
  void onCurrentSessionStarted(Project project);

  /**
   * When user comes back to application, and we should check if current session active or
   * not
   */
  void onCheckingTimerState(CurrentSessionModel sessionModel);

  /**
   * While time is is counting, and it should be displayed in text time
   *
   * @see TimeUtils#toStandardTime(long)
   */
  void onTimeCounting(String formattedTime);

  /**
   * When user pauses, resumes, or stops current session
   *
   * @see TimerState
   */
  void onCurrentSessionStateChanged(TimerState state);

  /**
   * When current session stopped
   */
  void onCurrentSessionEnded();
}
