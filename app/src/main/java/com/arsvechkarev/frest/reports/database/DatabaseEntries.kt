package com.arsvechkarev.frest.reports.database

import com.arsvechkarev.frest.projects.ProjectsManager
import com.arsvechkarev.frest.reports.database.readers.AllReportsReader
import com.arsvechkarev.frest.reports.database.readers.BasicDataReader
import com.arsvechkarev.frest.reports.database.readers.SpecProjectReader
import java.util.ArrayList

/**
 * This class contains queries and entries constants for database
 *
 * @author Arseniy Svechkarev
 * @see DataWriter
 * @see BasicDataReader
 * @see SpecProjectReader
 * @see AllReportsReader
 */
object DatabaseEntries {
  
  /**
   * Name of main table with task durations
   */
  const val TABLE_NAME = "Duration"
  
  /**
   * Name of temporary table, which need to store temporary table information when we
   * need to delete project
   */
  private const val TEMPORARY_NAME = "Duration_backup"
  
  /**
   * Name of column in [TABLE_NAME], which uses to save dates of tasks
   */
  const val DATE = "Date"
  
  /**
   * Main query with creating [TABLE_NAME]
   */
  const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($DATE TEXT)"
  
  /**
   * Main query with deleting [TABLE_NAME]
   */
  const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
  
  /**
   * Making query with creating new project column
   *
   * @param projectSerialCode Code of project. See [ProjectsManager] to know more
   */
  @JvmStatic
  fun queryCreateProjectColumn(projectSerialCode: String) =
      "ALTER TABLE $TABLE_NAME ADD COLUMN $projectSerialCode INTEGER DEFAULT 0"
  
  @JvmStatic
  fun queryDeleteProjectColumn(projectSerialCodes: ArrayList<String>): Array<String> {
    // Columns with old project names and comma
    val serialsBuilder = StringBuilder()
    // Columns with new project names default values and comma
    val createColumnBuilder = StringBuilder()
    
    if (projectSerialCodes.isNotEmpty()) {
      for (columnName in projectSerialCodes) {
        createColumnBuilder.append(", ").append(columnName).append(" INTEGER DEFAULT 0")
        serialsBuilder.append(", ").append(columnName)
      }
    }
    val projectColumns = serialsBuilder.toString()
    val columnsToCreate = createColumnBuilder.toString()
    return recreateTableQuery(projectColumns, columnsToCreate)
  }
  
  private fun recreateTableQuery(oldProjectColumns: String, columnsToCreate: String) =
      arrayOf(
        "BEGIN TRANSACTION;",
        "CREATE TABLE $TEMPORARY_NAME AS SELECT $DATE $oldProjectColumns FROM $TABLE_NAME;",
        "DROP TABLE $TABLE_NAME;",
        "CREATE TABLE $TABLE_NAME ($DATE TEXT $columnsToCreate);",
        "INSERT INTO $TABLE_NAME SELECT $DATE $oldProjectColumns FROM $TEMPORARY_NAME",
        "DROP TABLE $TEMPORARY_NAME;", "COMMIT;")
  
}
