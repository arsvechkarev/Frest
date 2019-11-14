package com.arsvechkarev.frest.starttask.newproject

/**
 * Interface for managing if project name that user entered to edit text is correct and it
 * can be saved to projects list and database
 *
 * @author Arseniy Svechkarev
 */
interface ProjectCreator {
  
  /**
   * When presenter need to save project with the name and the color
   */
  var projectColor: Int
  
  /**
   * Entered project name is correct, hiding warning
   */
  fun onAllowToSave()
  
  /**
   * Entered project name is not correct and we cannot save project
   *
   * @param reason Reason to know why saving has rejected
   * @see .NAME_CONSIST_JUST_OF_SPACES
   *
   * @see .NAME_IS_EMPTY
   *
   * @see .NAME_ALREADY_EXISTS
   */
  fun onRejectToSave(reason: Int)
  
  companion object {
    
    /**
     * Reason flag to send to activity message that entered project consist just of spaces
     */
    const val NAME_CONSIST_JUST_OF_SPACES = 1
    
    /**
     * Reason flag to send to activity message that entered project name is empty
     *
     * @see ProjectCreator.onRejectToSave
     */
    const val NAME_IS_EMPTY = 2
    
    /**
     * Reason flag to send to activity message that project with this name already exists
     *
     * @see ProjectCreator.onRejectToSave
     */
    const val NAME_ALREADY_EXISTS = 3
  }
}
