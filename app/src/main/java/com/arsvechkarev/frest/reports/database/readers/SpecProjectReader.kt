package com.arsvechkarev.frest.reports.database.readers

import android.content.Context
import com.arsvechkarev.frest.additional.datetime.Date
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.models.reports.DateDurationPair
import com.arsvechkarev.frest.reports.database.DatabaseEntries
import com.arsvechkarev.frest.reports.database.readers.BasicDataReader.Companion.getReadableDatabase

/**
 * Class that contains methods about retrieving stats by specific project
 *
 * @author Arseniy Svechkarev
 */
class SpecProjectReader(val context: Context) {
  
  /**
   * Returns stats of particular project from [dateFrom] to today
   */
  fun getProjectStats(serialCode: String, dateFrom: String): List<DateDurationPair> {
    val query = if (dateFrom == StringDateValues.ALL_TIME) {
      "SELECT ${DatabaseEntries.DATE},$serialCode FROM ${DatabaseEntries.TABLE_NAME} " +
          "ORDER BY ${DatabaseEntries.DATE}"
    } else {
      val today = StringDateValues.today()
      "SELECT ${DatabaseEntries.DATE},$serialCode FROM ${DatabaseEntries.TABLE_NAME} " +
          "WHERE ${DatabaseEntries.DATE} BETWEEN '$dateFrom' AND '$today' " +
          "ORDER BY ${DatabaseEntries.DATE}"
    }
    return retrieveProject(query, serialCode)
  }
  
  /** Retrieves project stats */
  private fun retrieveProject(query: String, columnName: String): List<DateDurationPair> {
    val database = getReadableDatabase(context)
    val cursor = database.rawQuery(query, null)
    val list = ArrayList<DateDurationPair>()
    while (cursor.moveToNext()) {
      val strDate = cursor.getString(cursor.getColumnIndex(DatabaseEntries.DATE))
      val dbDuration = cursor.getLong(cursor.getColumnIndex(columnName))
      list.add(
        DateDurationPair(Date(strDate), dbDuration))
    }
    cursor.close()
    database.close()
    return list
  }
}