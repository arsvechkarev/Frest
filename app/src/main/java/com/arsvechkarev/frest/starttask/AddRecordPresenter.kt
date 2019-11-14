package com.arsvechkarev.frest.starttask

import android.content.Intent
import android.os.Parcelable
import android.text.Spanned
import android.widget.DatePicker
import android.widget.TimePicker
import com.arsvechkarev.frest.additional.Codes.Key
import com.arsvechkarev.frest.additional.datetime.Date
import com.arsvechkarev.frest.additional.datetime.HALF_HOUR
import com.arsvechkarev.frest.additional.datetime.PrettyDate
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.additional.datetime.UNIT_DAY
import com.arsvechkarev.frest.additional.datetime.UNIT_MONTH
import com.arsvechkarev.frest.additional.datetime.UNIT_YEAR
import com.arsvechkarev.frest.additional.datetime.getDateFromUnits
import com.arsvechkarev.frest.additional.datetime.getDateUnit
import com.arsvechkarev.frest.additional.datetime.getSecondsFrom
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import java.lang.Integer.parseInt

/**
 * Presenter for [AddRecordActivity]
 *
 * @author Arseniy Svechkarev
 */
class AddRecordPresenter(
  /** Project from [AddRecordActivity] to save record */
  private val project: Project,
  
  /** Listener for sending callbacks to [AddRecordActivity] */
  private val actionsListener: RecordActionsListener
) {
  
  /** Date of record. Default value is today */
  private var date: String = StringDateValues.today()
  
  /** Duration of record. Default value is half of hour */
  private var duration: Long = HALF_HOUR
  
  /** For setting pretty view for date */
  private val prettyDate = PrettyDate()
  
  
  // ----- Sending TimePicker values -----
  
  /** Last selected hours for Time Picker */
  var durationHours: Int = 0
    private set
  /** Last selected minutes for Time Picker */
  var durationMinutes: Int = 0
    private set
  
  
  //  ----- Sending DatePicker values -----
  
  /** Last selected year for Date Picker */
  var dateYear: Int = 0
    private set
  
  /** Last selected day for Date Picker */
  var dateDay: Int = 0
    private set
  
  /**
   * Last selected month for Date Picker
   *
   * (Returns month - 1 because month count in class Date starts
   * with 0 instead of 1)
   */
  var dateMonth: Int = 0
  
  /**
   * Returns default text value for duration text
   */
  val defaultDurationView: Spanned
    get() = prettyDate.getPrettyDurationView(durationHours, durationMinutes)
  
  /**
   * Returns default text value for date text
   */
  val defaultDateView: String
    get() = prettyDate.getFullPrettyDateView(StringDateValues.today())
  
  init {
    // Duration defaults
    durationHours = DEFAULT_HOURS
    durationMinutes = DEFAULT_MINUTES
    
    // Date defaults
    dateYear = parseInt(getDateUnit(date, UNIT_YEAR))
    dateMonth = parseInt(getDateUnit(date, UNIT_MONTH))
    dateDay = parseInt(getDateUnit(date, UNIT_DAY))
  }
  
  /** When user sets duration in Time picker */
  @Suppress("UNUSED_PARAMETER")
  fun onTimeSet(view: TimePicker, hours: Int, minutes: Int) {
    // setting duration in seconds
    duration = getSecondsFrom(hours, minutes)
    if (duration == 0L) {
      // if duration = 0, record can't be saved
      actionsListener.onRejectToSave()
    } else {
      actionsListener.onAllowToSave()
    }
    
    durationHours = hours
    durationMinutes = minutes
    val prettyDuration = prettyDate.getPrettyDurationView(durationHours, durationMinutes)
    actionsListener.onDurationChanged(prettyDuration)
  }
  
  /** When user sets date in Date picker */
  @Suppress("UNUSED_PARAMETER")
  fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
    dateYear = year
    // +1 because month
    // count in class Date starts with 0 instead of 1
    dateMonth = month + 1
    dateDay = day
    
    date = getDateFromUnits(dateYear, dateMonth, dateDay)
    val prettyStrDate = prettyDate.getFullPrettyDateView(date)
    actionsListener.onDateChanged(prettyStrDate)
  }
  
  /** When user tries to save record */
  fun onSave() {
    val resultData = Intent()
    val task = Task(project, duration, Date(date))
    resultData.putExtra(Key.TASK, task as Parcelable)
    actionsListener.onSave(resultData)
  }
  
  companion object {
    const val DEFAULT_HOURS: Int = 0
    const val DEFAULT_MINUTES: Int = 30
  }
}
