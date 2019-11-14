package com.arsvechkarev.frest.additional.datetime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Class-container with different dates in [DatePattern.STANDARD]
 *
 * @author Arseniy Svechkarev
 */
object StringDateValues {
  
  const val ALL_TIME = "all_time"
  
  /**
   * Returns current date
   *
   * @see DatePattern.STANDARD
   */
  @JvmStatic
  fun today(): String {
    val dateFormat = SimpleDateFormat(DatePattern.STANDARD, Locale.US)
    return dateFormat.format(Date())
  }
  
  /**
   * Returns yesterday date
   */
  @JvmStatic
  fun yesterday() = editedDate(-1)
  
  /**
   * Returns 7 days before current date
   */
  @JvmStatic
  fun lastSevenDaysDate() = editedDate(-6)
  
  /**
   * Returns 30 days before current date
   */
  @JvmStatic
  fun lastThirtyDaysDate() = editedDate(-29)
  
  /**
   * Returns 365 days before current date
   */
  @JvmStatic
  fun lastYearDate() = editedDate(-365)
  
  /**
   * Returns current year
   */
  @JvmStatic
  fun currentYear() = getDateUnit(today(), UNIT_YEAR)
  
  private fun editedDate(amount: Int): String {
    val currentDate = today()
    val calendar = getCalendarInstanceDate(currentDate)
    calendar.add(Calendar.DATE, amount)
    return getStandardDateFormat().format(calendar.time)
  }
}
