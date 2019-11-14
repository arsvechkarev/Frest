package com.arsvechkarev.frest.home.managers;

import com.arsvechkarev.frest.models.main.Task;

/**
 * Interface for sending callbacks about timeline recycler item actions
 */
public interface TimelineActionsListener {

  /**
   * When user wants to continue already saved task
   */
  void onContinueTaskClick(Task task);

  /**
   * When user wants to view task stats
   */
  void onTaskClick(Task task);

  /**
   * When user wants to delete task
   */
  void onTaskLongClick(Task task);

}
