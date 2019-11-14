package com.arsvechkarev.frest.reports.database.readers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.arsvechkarev.frest.reports.database.DatabaseEntries
import com.arsvechkarev.frest.reports.database.DatabaseHelper

/**
 * Class which provides simple database reading logic. Including retrieving task duration
 * and checking, is particular date is exist in database or not
 *
 * @author Arseniy Svechkarev
 */
class BasicDataReader(val context: Context) {
  
  /**
   * Returns if date already exists in database or not
   */
  fun isDateExists(date: String): Boolean {
    val database = getReadableDatabase(context)
    val projection = arrayOf(DatabaseEntries.DATE)
    val selection = "${DatabaseEntries.DATE} = ?"
    val selectionArgs = arrayOf(date)
    
    val cursor = database.query(DatabaseEntries.TABLE_NAME,
      projection, selection, selectionArgs, null, null, null)
    
    var foundDate = ""
    if (cursor.moveToFirst()) {
      foundDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntries.DATE))
    }
    cursor.close()
    database.close()
    return foundDate == date
  }
  
  /**
   * Returns already saved tasks duration in database
   */
  fun getTaskDuration(projectName: String, date: String): Long {
    val database = getReadableDatabase(context)
    val projection = arrayOf(projectName)
    val selection = "${DatabaseEntries.DATE} = ?"
    val selectionArgs = arrayOf(date)
    
    val cursor = database.query(DatabaseEntries.TABLE_NAME,
      projection, selection, selectionArgs, null, null, null)
    
    var foundTime: Long = 0
    if (cursor.moveToFirst()) {
      foundTime = cursor.getLong(cursor.getColumnIndexOrThrow(projectName))
    }
    cursor.close()
    database.close()
    return foundTime
  }
  
  companion object {
    fun getReadableDatabase(context: Context): SQLiteDatabase = DatabaseHelper(
      context).readableDatabase
  }
}