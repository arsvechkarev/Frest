package com.arsvechkarev.frest.projects

import com.arsvechkarev.frest.models.main.Project

/**
 * Interface for checking what project user deletes
 *
 * @author Arseniy Svechkarev
 */
interface DeleteProjectListener {
  
  fun onDeleteProject(project: Project)
}