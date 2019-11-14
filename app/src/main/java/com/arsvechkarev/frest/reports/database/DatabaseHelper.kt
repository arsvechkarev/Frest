package com.arsvechkarev.frest.reports.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.arsvechkarev.frest.reports.database.readers.AllReportsReader
import com.arsvechkarev.frest.reports.database.readers.BasicDataReader
import com.arsvechkarev.frest.reports.database.readers.SpecProjectReader

/**
 * Main database helper
 *
 * @author Arseniy Svechkarev
 * @see DatabaseEntries
 * @see DataWriter
 * @see BasicDataReader
 * @see SpecProjectReader
 * @see AllReportsReader
 */
class DatabaseHelper(context: Context) :
  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  
  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(DatabaseEntries.SQL_CREATE_TABLE)
  }
  
  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL(DatabaseEntries.SQL_DELETE_TABLE)
  }
  
  companion object {
    private const val DATABASE_NAME = "TasksDuration"
    private const val DATABASE_VERSION = 1
  }
}
