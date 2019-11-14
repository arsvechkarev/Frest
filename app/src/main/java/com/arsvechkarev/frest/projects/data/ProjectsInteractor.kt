package com.arsvechkarev.frest.projects.data

import com.arsvechkarev.frest.additional.extenstions.PreferencesManager
import com.arsvechkarev.frest.additional.utils.getKey
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import java.util.LinkedHashMap

/**
 * Projects interactor that responsible for main projects functionality
 *
 * @author Arseniy Svechkarev
 */
class ProjectsInteractor(
  private val preferencesManager: PreferencesManager,
  private val repository: ProjectsRepository
) {
  
  companion object {
    private const val FILE_PROJECTS = "Projects"
    private const val PROJECT_SERIAL_NUMBER = "project_serial_number"
  }
  
  /**
   * Returns project serial code, which uses for naming column in database
   */
  suspend fun getSerialCode(project: Project): String {
    return getSerialCodeOrThrow(getProjects(), project)
  }
  
  /**
   * Like [getSerialCode], but takes [task] as a parameter
   */
  suspend fun getSerialCode(task: Task): String {
    val project = Project(task.name, task.color)
    return getSerialCodeOrThrow(getProjects(), project)
  }
  
  /**
   * Returns project name by project serial code, if we need to know, which project
   * corresponds to particular code
   */
  suspend fun getProjectBySerialCode(projectSerialCode: String): Project {
    val projects = getProjects()
    return if (projects == null || projects.getKey(projectSerialCode) == null) {
      throw IllegalArgumentException(
        "Project for key $projectSerialCode does not exists")
    } else {
      projects.getKey(projectSerialCode)!!
    }
  }
  
  /**
   * Checked if project with the [enteredName] is already exists
   */
  suspend fun isProjectAlreadyExists(enteredName: String): Boolean {
    val copyEnteredName = enteredName.trim()
    val projects = getProjects() ?: return false
    for (project in projects.keys) {
      if (project.name.equals(copyEnteredName, ignoreCase = true))
        return true
    }
    return false
  }
  
  /**
   * Save new project to file and database
   */
  suspend fun addNewProject(projectName: String, projectColor: Int) {
    val name = projectName.trim()
    val project = Project(name, projectColor)
    var serialNumber = preferencesManager.getInt(PROJECT_SERIAL_NUMBER, 0)
    preferencesManager.putInt(PROJECT_SERIAL_NUMBER, serialNumber++)
    val serialCode = "proj_$serialNumber"
    repository.saveProject(project, serialCode)
  }
  
  /**
   * Deletes particular [project] from list
   */
  suspend fun deleteProject(project: Project) {
    repository.deleteProject(project)
  }
  
  private suspend fun getProjects() = repository.getAllProjects(FILE_PROJECTS)
  
  private fun getSerialCodeOrThrow(
    map: LinkedHashMap<Project, String>?, project: Project
  ): String {
    return if (map == null || map[project] == null) {
      throw IllegalArgumentException("Project ${project.name} does not exists")
    } else {
      map[project]!!
    }
  }
  
}
