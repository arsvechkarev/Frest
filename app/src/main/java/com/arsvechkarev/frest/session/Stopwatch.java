package com.arsvechkarev.frest.session;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.arsvechkarev.frest.models.main.Project;

/**
 * Class for counting time
 *
 * // This is singleton
 *
 * @author Arseniy Svechkarev
 * @see SessionController
 */
class Stopwatch {

  /**
   * Message id for {@link #handler}
   */
  private static final int MSG = 29;

  /**
   * Current time of stopwatch
   */
  private long currentTime;

  /**
   * Started time for stopwatch
   */
  private long baseTime;

  /**
   * Current state of timer
   */
  private TimerState state = TimerState.INACTIVE;

  /**
   * Current active project
   */
  private Project project;

  /**
   * Interface for updating time
   */
  private TimeTickListener tickListener;

  private static Stopwatch instance;

  private Stopwatch() {
    // required private constructor
  }

  static Stopwatch getInstance() {
    if (instance == null) {
      instance = new Stopwatch();
    }
    return instance;
  }

  void setTimeTickListener(TimeTickListener tickListener) {
    this.tickListener = tickListener;
  }

  void setCurrentProject(Project project) {
    this.project = project;
  }

  void start() {
    baseTime = SystemClock.elapsedRealtime();
    handler.sendMessage(handler.obtainMessage(MSG));
    state = TimerState.ACTIVE;
  }

  void pause() {
    state = TimerState.PAUSED;
  }

  void resume() {
    baseTime = SystemClock.elapsedRealtime() - currentTime;
    handler.sendMessage(handler.obtainMessage(MSG));
    state = TimerState.ACTIVE;
  }

  void stop() {
    handler.removeMessages(MSG);
    state = TimerState.INACTIVE;
  }

  long getTime() {
    return currentTime / 1000;
  }

  void reset() {
    currentTime = 0;
    baseTime = 0;
    project = null;
  }

  TimerState getState() {
    return state;
  }

  @Nullable
  Project getCurrentProject() {
    return project;
  }

  @SuppressLint("HandlerLeak")
  private final Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      synchronized (Stopwatch.this) {
        if (state != TimerState.ACTIVE) {
          return;
        }
        long executionStartedTime = SystemClock.elapsedRealtime();
        currentTime = SystemClock.elapsedRealtime() - baseTime;
        tickListener.onTimeTick(currentTime / 1000);
        long executionDelay = SystemClock.elapsedRealtime() - executionStartedTime;
        sendMessageDelayed(obtainMessage(MSG), 1000 - executionDelay);
      }
    }
  };

  /**
   * Interface for watching the time
   */
  public interface TimeTickListener {

    void onTimeTick(long currentSecondsTime);
  }
}
