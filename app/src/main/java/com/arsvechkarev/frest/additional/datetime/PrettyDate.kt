package com.arsvechkarev.frest.additional.datetime

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.App

/**
 * Class which represents methods for pretty date/duration views
 *
 * @author Arseniy Svechkarev
 */
class PrettyDate {
  
  // Context for accessing to strings
  private val context: Context = App.context
  
  /** Hour postfix for duration view */
  private val hourPostfix: String
  
  /** Minute postfix for duration view */
  private val minutePostfix: String
  
  /** Word "Today" */
  private val today: String
  
  /** Word "Yesterday" */
  private val yesterday: String
  
  /**
   * Getting all strings from resources (postfixes, "Today" and "Yesterday words)
   */
  init {
    hourPostfix = context.resources.getString(R.string.postfix_hour)
    minutePostfix = context.resources.getString(R.string.postfix_minute)
    today = context.resources.getString(R.string.today)
    yesterday = context.resources.getString(R.string.yesterday)
  }
  
  /**
   * Returns pretty view of hours and minutes
   * ```
   * Example:
   * 1)  hours = 0, minutes = 20, --> 20 min
   * 2)  hours = 1, minutes = 35, --> 1h 35min
   * ```
   */
  fun getPrettyDurationView(hours: Int, minutes: Int): Spanned {
    return if (hours == 0) {
      HtmlCompat.fromHtml("<b>$minutes</b>$minutePostfix", FROM_HTML_MODE_LEGACY)
    } else HtmlCompat.fromHtml(
      "<b>" + hours + "</b>" + hourPostfix + " <b>"
          + minutes + "</b>" + minutePostfix, FROM_HTML_MODE_LEGACY)
  }
  
  /**
   * Returns pretty date view for timeline
   */
  fun getPrettyDateView(date: String): String {
    return getDateView(date, DATE_PARTICULAR)
  }
  
  /**
   * Returns pretty date view for date picker
   */
  fun getFullPrettyDateView(date: String): String {
    return getDateView(date, DATE_FULL)
  }
  
  private fun getDateView(date: String, dateType: Int): String {
    if (dateType == DATE_PARTICULAR) {
      return when (date) {
        StringDateValues.today() -> today
        StringDateValues.yesterday() -> yesterday
        else -> {
          if (StringDateValues.currentYear() == getDateUnit(date, UNIT_YEAR)) {
            getFormattedDate(date, DatePattern.PRETTY)
          } else {
            getFormattedDate(date, DatePattern.PRETTY_YEAR)
          }
        }
      }
    } else if (dateType == DATE_FULL) {
      return if (StringDateValues.currentYear() == getDateUnit(date, UNIT_YEAR)) {
        getFormattedDate(date, DatePattern.FULL)
      } else {
        getFormattedDate(date, DatePattern.FULL_YEAR)
      }
    }
    throw IllegalArgumentException("Illegal date type")
  }
  
  private fun getFormattedDate(date: String, pattern: String): String {
    val calendar = getCalendarInstanceDate(date)
    return getDateFormat(pattern).format(calendar.time)
  }
  
  companion object {
    
    /** If needs particular view of date, like "Fri, Jun 7" or "Fri, Jun 7, 2019" */
    private const val DATE_PARTICULAR = 0
    
    /** If needs full view of date, like "Friday, Jun 7" or "Friday, Jun 7, 2019" */
    private const val DATE_FULL = 1
  }
}
