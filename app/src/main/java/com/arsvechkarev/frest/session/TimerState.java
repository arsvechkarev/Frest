package com.arsvechkarev.frest.session;

/**
 * Enum for checking current state of {@link Stopwatch}
 *
 * @author Arseniy Svechkarev
 */
public enum TimerState {
  /**
   * When timer is active and counting time
   */
  ACTIVE,

  /**
   * When time is active, but paused
   */
  PAUSED,

  /**
   * When timer is stopped
   */
  INACTIVE
}
