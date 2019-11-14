@file:JvmName("DateUtils")

package com.arsvechkarev.frest.additional.datetime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val DAYS_PER_CYCLE = 146097L

/**
 * Number of days from start of era to 1970 year
 */
private const val DAYS_0000_TO_1970 = 719528L

/**
 * Year of date
 */
const val UNIT_YEAR = 4

/**
 * Month of date
 */
const val UNIT_MONTH = 5

/**
 * Day of date
 */
const val UNIT_DAY = 6

/**
 * Returns unit (year, month, day) of particular date
 *
 * @see UNIT_YEAR
 * @see UNIT_MONTH
 * @see UNIT_DAY
 */
fun getDateUnit(date: String, unit: Int): String {
  val units = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  return when (unit) {
    UNIT_YEAR -> units[0]
    UNIT_MONTH -> units[1]
    UNIT_DAY -> units[2]
    else -> throw IllegalArgumentException("Illegal unit type")
  }
}

/**
 * Getting year, month and day and returns date in [DatePattern.STANDARD]
 */
fun getDateFromUnits(year: Int, month: Int, day: Int): String {
  val strMonth = if (month < 10) "0$month" else month.toString()
  val strDay = if (day < 10) "0$day" else day.toString()
  return "$year-$strMonth-$strDay"
}

fun getDateFormat(pattern: String): SimpleDateFormat {
  return SimpleDateFormat(pattern, Locale.US)
}

fun getStandardDateFormat(): SimpleDateFormat {
  return SimpleDateFormat(DatePattern.STANDARD, Locale.US)
}

fun getCalendarInstanceDate(strDate: String): Calendar {
  val calendar = Calendar.getInstance()
  val date = getStandardDateFormat().parse(strDate)
  calendar.time = date
  return calendar
}

/**
 * Converts string date to float date
 */
fun Date.toNumber(): Float {
  val year = getDateUnit(stringDate, UNIT_YEAR)
  val month = getDateUnit(stringDate, UNIT_MONTH)
  val day = getDateUnit(stringDate, UNIT_DAY)
  return getEpochDay(year.toLong(), month.toLong(), day.toLong())
}

/**
 * Converts float date in graph without instantiate data object for better performance
 */
fun Float.toChartDate(): String {
  val strDate = getDateFromEpochDay(this.toLong())
  return getDateFormat(DatePattern.CHART)
      .format(getCalendarInstanceDate(strDate).time)
}


fun Float.toFullChartDate(): String {
  val strDate = getDateFromEpochDay(this.toLong())
  val year = getDateUnit(strDate, UNIT_YEAR)
  val pattern = if (year == StringDateValues.currentYear()) {
    DatePattern.CHART
  } else {
    DatePattern.CHART_YEAR
  }
  return getDateFormat(pattern).format(getCalendarInstanceDate(strDate).time)
}

/**
 * Adding one day to current date without instantiate date object
 */
fun String.addDay(): String {
  return changeDaysBy(1)
}

fun String.changeDaysBy(amount: Int): String {
  val inputFormat = SimpleDateFormat(DatePattern.STANDARD, Locale.US)
  val outputFormat = SimpleDateFormat(DatePattern.STANDARD, Locale.US)
  
  val resDate = inputFormat.parse(this)
  val calendar = Calendar.getInstance()
  calendar.time = resDate
  calendar.add(Calendar.DATE, amount)
  return outputFormat.format(calendar.time)
}

private fun getEpochDay(year: Long, month: Long, day: Long): Float {
  var total = 0L
  total += 365L * year
  if (year >= 0L) {
    total += (year + 3L) / 4L - (year + 99L) / 100L + (year + 399L) / 400L
  } else {
    total -= year / -4L - year / -100L + year / -400L
  }
  total += (367L * month - 362L) / 12L
  total += (day - 1)
  if (month > 2L) {
    --total
    if (!isLeapYear(year)) {
      --total
    }
  }
  return (total - DAYS_0000_TO_1970).toFloat()
}

private fun getDateFromEpochDay(epochDay: Long): String {
  var zeroDay = epochDay + DAYS_0000_TO_1970
  zeroDay -= 60L
  var adjust = 0L
  var yearEst: Long
  if (zeroDay < 0L) {
    yearEst = (zeroDay + 1L) / DAYS_PER_CYCLE - 1L
    adjust = yearEst * 400L
    zeroDay += -yearEst * DAYS_PER_CYCLE
  }
  
  yearEst = (400L * zeroDay + 591L) / DAYS_PER_CYCLE
  var doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L)
  if (doyEst < 0L) {
    --yearEst
    doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L)
  }
  
  yearEst += adjust
  val marchDoy0 = doyEst.toInt()
  val marchMonth0 = (marchDoy0 * 5 + 2) / 153
  val month = (marchMonth0 + 2) % 12 + 1
  val dom = marchDoy0 - (marchMonth0 * 306 + 5) / 10 + 1
  yearEst += (marchMonth0 / 10).toLong()
  return getDateFromUnits(yearEst.toInt(), month, dom)
}

private fun isLeapYear(year: Long): Boolean {
  return year and 3L == 0L && (year % 100L != 0L || year % 400L == 0L)
}

