package com.arsvechkarev.frest.starttask.newproject

import com.arsvechkarev.frest.additional.ProjectColors
import com.arsvechkarev.frest.additional.utils.consistOfSpaces
import com.arsvechkarev.frest.projects.ProjectsManager
import com.arsvechkarev.frest.starttask.newproject.ProjectCreator.Companion.NAME_ALREADY_EXISTS
import com.arsvechkarev.frest.starttask.newproject.ProjectCreator.Companion.NAME_CONSIST_JUST_OF_SPACES
import com.arsvechkarev.frest.starttask.newproject.ProjectCreator.Companion.NAME_IS_EMPTY

/**
 * Presenter for [NewProjectActivity]
 *
 * @author Arseniy Svechkarev
 */
class NewProjectPresenter(
  
  /**
   * Listener to send callbacks to [NewProjectActivity]
   */
  private var projectCreator: ProjectCreator?,
  
  /**
   * Projects manager to saving project
   */
  private var projectsManager: ProjectsManager?
) {
  
  /**
   * When user finished entering text in edit text
   *
   * @param projectName Text that user entered
   */
  fun afterTextChanged(projectName: String) {
    when {
      projectName.consistOfSpaces() -> projectCreator?.onRejectToSave(NAME_CONSIST_JUST_OF_SPACES)
      projectName.isEmpty() -> projectCreator?.onRejectToSave(NAME_IS_EMPTY)
      projectsManager!!.isProjectAlreadyExists(projectName) -> projectCreator?.onRejectToSave(
        NAME_ALREADY_EXISTS)
      else -> projectCreator?.onAllowToSave()
    }
  }
  
  fun saveProject(name: String) {
    val color = projectCreator?.projectColor ?: ProjectColors.RED.color
    projectsManager?.addNewProject(name, color)
  }
  
  fun detachView() {
    projectCreator = null
    projectsManager = null
  }
  
}
