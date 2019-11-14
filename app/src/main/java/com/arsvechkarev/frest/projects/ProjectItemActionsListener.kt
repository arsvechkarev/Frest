package com.arsvechkarev.frest.projects

import com.arsvechkarev.frest.models.main.Project

/**
 * Interface for creating callback, which action user makes with project
 *
 * @author Arseniy Svechkarev
 * @see ProjectsListFragment
 */
interface ProjectItemActionsListener {
  
  /**
   * When user makes standard click on project item
   */
  fun onProjectItemClick(project: Project)
  
  /**
   * When user makes long click on project item
   */
  fun onProjectItemLongClick(project: Project)
}