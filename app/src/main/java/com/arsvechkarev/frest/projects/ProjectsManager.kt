package com.arsvechkarev.frest.projects

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.arsvechkarev.frest.additional.utils.inBackground
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import com.arsvechkarev.frest.projects.ProjectsManager.Companion.PROJECTS_DATA
import com.arsvechkarev.frest.projects.ProjectsManager.Companion.PROJECTS_MAP
import com.arsvechkarev.frest.reports.database.DataWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap

/**
 * Class for managing the projects. Projects are saved in [HashMap], where key is
 * [Project] instance, and value is **Project serial code**. This code is used to
 * save project in database, because project name can contains any symbols that not
 * supported in SQLite.
 *
 * Serial code formats here: [formatProjectSerialCode]
 *
 * Project HashMap saved in [projectsFile] by [ObjectOutputStream] and
 * [ObjectInputStream]. See [PROJECTS_DATA], [PROJECTS_MAP] to know more
 *
 * @author Arseniy Svechkarev
 *
 * @see Project
 * @see ProjectsAdapter
 * @see ProjectsListFragment
 */
class ProjectsManager(private val context: Context) {
  
  /** File with projects*/
  private val projectsFile: File
  
  // Preferences to save serial number of project
  private val preferences: SharedPreferences
  
  /**  Main map with projects and serial codes */
  private var projectsMap: LinkedHashMap<Project, String>? = null
  
  init {
    preferences = context.getSharedPreferences(PREFERENCES_SERIAL_NUMBER, MODE_PRIVATE)
    projectsFile = File(context.getDir(PROJECTS_DATA, MODE_PRIVATE), PROJECTS_MAP)
    retrieveFile()
  }
  
  /** Returns ArrayList with all projects */
  val allProjects: ArrayList<Project>?
    get() {
      if (projectsMap == null) {
        return null
      }
      val projectsList = ArrayList<Project>(projectsMap!!.size)
      projectsList.addAll(projectsMap!!.keys)
      return projectsList
    }
  
  /** Retrieves map from file to work with it */
  fun retrieveFile() {
    projectsMap = getProjectsMap()
  }
  
  /** Returns project serial code, which uses for naming column in database */
  fun getProjectSerialCode(project: Project): String {
    return getSerialCodeOrThrow(projectsMap, project)
  }
  
  /** Like [getProjectSerialCode], but takes [Task] instance as a parameter */
  fun getProjectSerialCodeByTask(task: Task): String {
    val tempProject = Project(task.name, task.color)
    return getSerialCodeOrThrow(projectsMap, tempProject)
  }
  
  /**
   * Returns project name by project serial code, if we need to know, which project
   * corresponds to particular code
   */
  fun getProjectBySerialCode(projectSerialCode: String): Project {
    return if (projectsMap == null || getKey(projectsMap!!, projectSerialCode) == null) {
      throw IllegalArgumentException(
        "Project for key $projectSerialCode does not exists")
    } else {
      getKey(projectsMap!!, projectSerialCode)!!
    }
  }
  
  /** Checked if project with the [enteredName] is already exists */
  fun isProjectAlreadyExists(enteredName: String): Boolean {
    val copyEnteredName = enteredName.trim()
    if (projectsMap == null)
      return false
    for (project in projectsMap!!.keys) {
      if (project.name.equals(copyEnteredName, ignoreCase = true))
        return true
    }
    return false
  }
  
  /** Adding new project to projects map */
  fun addNewProject(projectName: String, projectColor: Int) {
    val copyProjectName = projectName.trim()
    val project = Project(copyProjectName, projectColor)
    if (projectsMap == null) {
      projectsMap = LinkedHashMap()
    }
    projectsMap!![project] = formatProjectSerialCode()
    saveProjectsMap(projectsMap!!)
    saveProjectToDatabase(project)
  }
  
  /** Deleted particular [project] from list */
  fun deleteProject(project: Project) {
    if (projectsMap != null) {
      // Removing from database
      val serialCodesList = getProjectSerialCodes(projectsMap!!)
      serialCodesList.remove(projectsMap!![project])
      deleteFromDatabase(serialCodesList)
      // Removing from list
      projectsMap!!.remove(project)
      saveProjectsMap(projectsMap!!)
    } else {
      throw IllegalStateException("Attempt to delete from empty project list")
    }
  }
  
  /**
   * Creates new serial code of project. Should invokes only when new project creates in
   * [addNewProject]. Serial code looks like "proj_X", where "X" is
   * serial number of current project
   */
  private fun formatProjectSerialCode(): String {
    var serialNumber = preferences.getInt(PROJECT_SERIAL_NUMBER, 0)
    val editor = preferences.edit()
    editor.putInt(PROJECT_SERIAL_NUMBER, ++serialNumber)
    editor.apply()
    return "proj_$serialNumber"
  }
  
  // Saves map to file
  private fun saveProjectsMap(projectsMap: LinkedHashMap<Project, String>) {
    ObjectOutputStream(FileOutputStream(projectsFile)).apply {
      writeObject(projectsMap)
      flush()
      close()
    }
  }
  
  // Returns map from file
  @Suppress("UNCHECKED_CAST")
  private fun getProjectsMap(): LinkedHashMap<Project, String>? {
    return if (projectsFile.exists()) {
      val inputStream = ObjectInputStream(FileInputStream(projectsFile))
      val projectsMap = inputStream.readObject() as? LinkedHashMap<Project, String>
      inputStream.close()
      projectsMap
    } else {
      null
    }
  }
  
  private fun saveProjectToDatabase(project: Project) {
    inBackground { DataWriter(context).saveProject(getProjectSerialCode(project)) }
  }
  
  private fun deleteFromDatabase(serialCodesList: ArrayList<String>) {
    inBackground { DataWriter(context).deleteProject(serialCodesList) }
  }
  
  /** Returns all projects serial codes in hash map */
  private fun getProjectSerialCodes(
    projectsMap: LinkedHashMap<Project, String>): ArrayList<String> {
    val serialCodesList = ArrayList<String>(projectsMap.size)
    serialCodesList.addAll(projectsMap.values)
    return serialCodesList
  }
  
  /**
   * Returns project from projects map or throws exception
   */
  private fun getSerialCodeOrThrow(
    map: LinkedHashMap<Project, String>?, project: Project
  ): String {
    return if (map == null || map[project] == null) {
      throw IllegalArgumentException("Project ${project.name} does not exists")
    } else {
      map[project]!!
    }
  }
  
  companion object {
    
    /** Name of file directory where projects are saved */
    private const val PROJECTS_DATA = "projects_file_data"
    
    /** Name of file with projects map */
    private const val PROJECTS_MAP = "projects_map"
    
    /** Preferences file name to save serial number of project */
    private const val PREFERENCES_SERIAL_NUMBER = "preferences_serial_number"
    
    /** Key for saving serial number of project */
    private const val PROJECT_SERIAL_NUMBER = "projects_serial_number"
    
    /** Returns key in [map] by [value], or null if it wasn't found */
    private fun <K, V> getKey(map: Map<K, V>, value: V): K? {
      for (key in map.keys) {
        if (value == map[key]) {
          return key
        }
      }
      throw IllegalArgumentException("Key for value $value does not exists")
    }
  }
}
