package com.arsvechkarev.frest.reports.database.readers

import android.content.Context
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.reports.ReportsTriple
import com.arsvechkarev.frest.projects.ProjectsManager
import com.arsvechkarev.frest.reports.database.DatabaseEntries
import com.arsvechkarev.frest.reports.database.readers.BasicDataReader.Companion.getReadableDatabase

/**
 * Class that contains methods about retrieving all activities stats
 *
 * @author Arseniy Svechkarev
 */
class AllReportsReader(val context: Context) {
  
  /**
   * Returns stats by all projects from [dateFrom] to today
   */
  fun getAllStatsFrom(dateFrom: String): List<ReportsTriple> {
    val dateTo = StringDateValues.today()
    val query = "SELECT * FROM ${DatabaseEntries.TABLE_NAME} WHERE ${DatabaseEntries.DATE}" +
        " BETWEEN '$dateFrom' AND '$dateTo'"
    return retrieveStats(query)
  }
  
  /**
   * Returns stats by all projects for all time
   */
  fun getAllStats(): List<ReportsTriple> {
    val query = "SELECT * FROM ${DatabaseEntries.TABLE_NAME}"
    return retrieveStats(query)
  }
  
  private fun retrieveStats(query: String): List<ReportsTriple> {
    val database = getReadableDatabase(context)
    val cursor = database.rawQuery(query, null)
    
    val projectsManager = ProjectsManager(context)
    val projectColumns = Array(cursor.columnCount - 1) { i ->
      cursor.getColumnName(i + 1)
    }
    val projects = getDefaultList(projectsManager, projectColumns)
    
    while (cursor.moveToNext()) {
      for (i in projectColumns.indices) {
        val dbDuration = cursor.getLong(cursor.getColumnIndexOrThrow(projectColumns[i]))
        val serialCode = projectColumns[i]
        if (projects[i].project.name == projectsManager.getProjectBySerialCode(serialCode).name) {
          projects[i].duration += dbDuration
        }
      }
    }
    cursor.close()
    database.close()
    return projects
  }
  
  /** Returns [ReportsTriple] list with default values */
  private fun getDefaultList(
    projectsManager: ProjectsManager,
    projectColumns: Array<String>
  ): List<ReportsTriple> {
    val list = ArrayList<ReportsTriple>()
    projectColumns.forEach {
      list.add(
        ReportsTriple(Project(
          projectsManager.getProjectBySerialCode(it).name,
          projectsManager.getProjectBySerialCode(it).color
        ), 0L)
      )
    }
    return list
  }
}