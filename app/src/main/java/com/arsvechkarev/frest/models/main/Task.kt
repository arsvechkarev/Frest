package com.arsvechkarev.frest.models.main

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.arsvechkarev.frest.additional.datetime.Date
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.additional.datetime.toStandardTime
import java.io.Serializable
import java.util.Objects

/**
 * This class is an extended version of [.Project]. Class Project has a name
 * and color, but Task also has a [.duration] and [.date] When user ends task,
 * it saves to database and tasks list with date and duration. And user can see in
 * reports, how many time he spend for particular project. (Duration of project is a sum
 * of duration of all its tasks)
 *
 * @author Arseniy Svechkarev
 * @see Project
 */
@Keep
class Task : Project, Comparable<Task>, Serializable, Parcelable {
  
  /**
   * Day when task occurred
   */
  val date: String
  
  /**
   * Duration of task (in seconds)
   */
  val duration: Long
  
  val formattedDuration: String
    get() = duration.toStandardTime()
  
  /**
   * Standard constructor, creates task when user finishes session
   *
   * @param project Project which relates with the task
   * @param duration Duration in seconds
   */
  constructor(project: Project, duration: Long) : super(project.name, project.color) {
    this.duration = duration
    this.date = StringDateValues.today()
  }
  
  /**
   * Constructor for creating task when user adds record manually
   *
   * @param project Project which relates with the task
   * @param duration Duration in seconds
   * @param date Date of task in [Date] instance to check date format
   */
  constructor(project: Project, duration: Long, date: Date) : super(project.name, project.color) {
    this.duration = duration
    this.date = date.stringDate
  }
  
  /**
   * Constructor for creating an updated task by other task parameters
   *
   * @param name Previous task name
   * @param color Previous task color
   * @param duration Duration in seconds
   * @param date Date of task in [Date] instance to check date format
   */
  constructor(name: String, color: Int, duration: Long, date: Date) : super(name, color) {
    this.duration = duration
    this.date = date.stringDate
  }
  
  protected constructor(parcel: Parcel) : super(parcel) {
    date = parcel.readString()!!
    duration = parcel.readLong()
  }
  
  override fun compareTo(other: Task): Int {
    return this.date.compareTo(other.date)
  }
  
  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other == null || javaClass != other.javaClass) {
      return false
    }
    if (!super.equals(other)) {
      return false
    }
    val taskModel = other as Task?
    return duration == taskModel!!.duration && date == taskModel.date
  }
  
  override fun hashCode(): Int {
    return Objects.hash(super.hashCode(), date, duration)
  }
  
  override fun writeToParcel(dest: Parcel, flags: Int) {
    super.writeToParcel(dest, flags)
    dest.writeString(date)
    dest.writeLong(duration)
  }
  
  override fun describeContents(): Int {
    return 0
  }
  
  companion object {
    
    private const val serialVersionUID = 1L
    
    @JvmField
    val CREATOR: Parcelable.Creator<Task> = object : Parcelable.Creator<Task> {
      override fun createFromParcel(`in`: Parcel): Task {
        return Task(`in`)
      }
      
      override fun newArray(size: Int): Array<Task?> {
        return arrayOfNulls(size)
      }
    }
  }
}
