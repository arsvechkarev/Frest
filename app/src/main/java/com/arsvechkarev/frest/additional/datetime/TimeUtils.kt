@file:JvmName("TimeUtils")

package com.arsvechkarev.frest.additional.datetime

import android.content.Context
import com.arsvechkarev.frest.R

/** Half-hour in seconds */
const val HALF_HOUR: Long = 30 * 60

/** Num of seconds in hour */
const val SECONDS_IN_HOUR = 60 * 60

/** Num of seconds in minute */
const val SECONDS_IN_MINUTE = 60

/**
 * Converts duration in seconds to float hour representation to display in chart
 */
fun Long.toHour(): Float = (this.toDouble() / SECONDS_IN_HOUR).toFloat()

/**
 * Converts float hours to long duration seconds
 */
fun Float.toSeconds(): Long = (this * SECONDS_IN_HOUR).toLong()

/**
 * Returns num of seconds in received [hours] and [minutes]
 */
fun getSecondsFrom(hours: Int, minutes: Int): Long =
    (hours * SECONDS_IN_HOUR + minutes * SECONDS_IN_MINUTE).toLong()

fun Long.toStandardTime(): String {
  val durationUnits = getDurationUnits(this)
  
  val strRemSeconds = if (durationUnits.remSeconds < 10) {
    "0${durationUnits.remSeconds}"
  } else {
    durationUnits.remSeconds.toString()
  }
  
  val strRemMinutes = if (durationUnits.remMinutes < 10) {
    "0${durationUnits.remMinutes}"
  } else {
    durationUnits.remMinutes.toString()
  }
  
  return if (durationUnits.minutes == 0L) {
    "00:$strRemSeconds"
  } else {
    if (durationUnits.hours == 0L) {
      "$strRemMinutes:$strRemSeconds"
    } else {
      "${durationUnits.hours}:$strRemMinutes:$strRemSeconds"
    }
  }
}

fun Long.toPrettyTime(context: Context): String {
  val hourPostfix = context.resources.getString(R.string.postfix_hour)
  val minutePostfix = context.resources.getString(R.string.postfix_minute)
  val secondsPostfix = context.resources.getString(R.string.postfix_second)
  
  val durationUnits = getDurationUnits(this)
  
  val strHours = if (durationUnits.hours != 0L) {
    if (durationUnits.hours < 10) {
      "0${durationUnits.hours}"
    } else {
      "${durationUnits.hours}"
    }
  } else ""
  
  val strMinutes = if (durationUnits.remMinutes < 10) {
    "0${durationUnits.remMinutes}"
  } else {
    "${durationUnits.remMinutes}"
  }
  
  val strSeconds = if (durationUnits.remSeconds < 10) {
    "0${durationUnits.remSeconds}"
  } else {
    "${durationUnits.remSeconds}"
  }
  
  return if (strHours.isNotEmpty()) {
    "$strHours$hourPostfix  $strMinutes$minutePostfix  $strSeconds$secondsPostfix"
  } else {
    "$strMinutes$minutePostfix  $strSeconds$secondsPostfix"
  }
}

private fun getDurationUnits(seconds: Long): DurationUnits {
  val minutes = seconds / SECONDS_IN_MINUTE
  val remSeconds = seconds - minutes * SECONDS_IN_MINUTE
  val hours = minutes / SECONDS_IN_MINUTE
  val remMinutes = minutes - hours * SECONDS_IN_MINUTE
  return DurationUnits(hours, minutes, remMinutes, remSeconds)
}

class DurationUnits(
  val hours: Long,
  val minutes: Long,
  val remMinutes: Long,
  val remSeconds: Long
)
