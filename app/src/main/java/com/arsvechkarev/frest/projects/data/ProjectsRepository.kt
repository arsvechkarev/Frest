package com.arsvechkarev.frest.projects.data

import com.arsvechkarev.frest.models.main.Project

/**
 * Repository responsible for creating, reading, updating, deleting project from projects list
 */
interface ProjectsRepository {
  
  /**
   * Deletes project from file and database
   */
  suspend fun deleteProject(project: Project)
  
  /**
   * Returns map (project to serial code)
   */
  suspend fun getAllProjects(fileProjects: String): LinkedHashMap<Project, String>?
  
  /**
   * Save project to database and file
   */
  suspend fun saveProject(project: Project, serialCode: String)
}