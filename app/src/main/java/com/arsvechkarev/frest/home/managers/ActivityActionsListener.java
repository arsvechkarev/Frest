package com.arsvechkarev.frest.home.managers;

import android.content.Intent;
import com.arsvechkarev.frest.home.views.MainActivity;

/**
 * Interface for sending callbacks to {@link MainActivity}
 *
 * @author Arseniy Svechkarev
 */
public interface ActivityActionsListener {

  /**
   * When user wants to start new task
   */
  void startActivityForResult();

  /**
   * When user wants to starts new session
   */
  void startSessionService(Intent serviceIntent);

  /**
   * When user stops session
   */
  void stopSessionService(Intent serviceIntent);
}