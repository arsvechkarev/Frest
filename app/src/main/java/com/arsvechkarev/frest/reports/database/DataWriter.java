package com.arsvechkarev.frest.reports.database;

import static com.arsvechkarev.frest.reports.database.DatabaseEntries.queryDeleteProjectColumn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.arsvechkarev.frest.models.main.Task;
import com.arsvechkarev.frest.projects.ProjectsManager;
import com.arsvechkarev.frest.reports.database.readers.AllReportsReader;
import com.arsvechkarev.frest.reports.database.readers.BasicDataReader;
import com.arsvechkarev.frest.reports.database.readers.SpecProjectReader;
import java.util.ArrayList;

/**
 * This class is uses to insert, delete and updateBy data in the database.
 *
 * @author Arseniy Svechkarev
 * @see DatabaseEntries
 * @see DatabaseHelper
 * @see BasicDataReader
 * @see SpecProjectReader
 * @see AllReportsReader
 */
public class DataWriter {

  // Context for accessing to database
  private final Context context;

  public DataWriter(Context context) {
    this.context = context;
  }

  /**
   * Saving task to database when user finishes session or adds record
   */
  public void saveTask(Task task) {
    ProjectsManager manager = new ProjectsManager(context);
    String tableName = manager.getProjectSerialCodeByTask(task);
    String date = task.getDate();
    long duration = task.getDuration();
    if (isDateExists(date)) {
      updateTaskDuration(tableName, duration, date);
    } else {
      insertDate(date);
      updateTaskDuration(tableName, duration, date);
    }
  }

  /**
   * Removes info about task duration from database
   */
  public void removeTask(Task task) {
    ProjectsManager projectsManager = new ProjectsManager(context);
    String tableName = projectsManager.getProjectSerialCodeByTask(task);
    ContentValues valuesTask = new ContentValues();
    valuesTask.put(tableName, 0);

    SQLiteDatabase database = getWritableDatabase();
    database.update(DatabaseEntries.TABLE_NAME, valuesTask,
        DatabaseEntries.DATE + " = ?", new String[]{task.getDate()});
    database.close();
  }

  /**
   * Saving project to database and create new column with hash name of the project
   *
   * See {@link ProjectsManager} to know what is projectSerialCode
   */
  public void saveProject(String projectSerialCode) {
    SQLiteDatabase database = getWritableDatabase();
    database.execSQL(DatabaseEntries.queryCreateProjectColumn(projectSerialCode));
    database.close();
  }

  /**
   * Deletes project from database and its column
   *
   * @param serialCodes Table name of project in database. See {@link ProjectsManager} to
   * know more
   */
  public void deleteProject(ArrayList<String> serialCodes) {
    SQLiteDatabase database = getWritableDatabase();
    for (String query : queryDeleteProjectColumn(serialCodes)) {
      database.execSQL(query);
    }
    database.close();
  }

  private void insertDate(String date) {
    SQLiteDatabase database = getWritableDatabase();
    ContentValues valuesDate = new ContentValues();
    valuesDate.put(DatabaseEntries.DATE, date);
    database.insert(DatabaseEntries.TABLE_NAME, null, valuesDate);
    database.close();
  }

  private void updateTaskDuration(String tableName, long newDuration, String date) {
    BasicDataReader reader = new BasicDataReader(context);
    long previousDuration = reader.getTaskDuration(tableName, date);
    long totalTime = newDuration + previousDuration;

    ContentValues valuesTask = new ContentValues();
    valuesTask.put(tableName, totalTime);

    SQLiteDatabase database = getWritableDatabase();
    database.update(DatabaseEntries.TABLE_NAME, valuesTask,
        DatabaseEntries.DATE + " = ?", new String[]{date});
    database.close();
  }

  private boolean isDateExists(String date) {
    BasicDataReader reader = new BasicDataReader(context);
    return reader.isDateExists(date);
  }

  private SQLiteDatabase getWritableDatabase() {
    DatabaseHelper dbHelper = new DatabaseHelper(context);
    return dbHelper.getWritableDatabase();
  }
}
