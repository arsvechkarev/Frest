package com.arsvechkarev.frest.additional.datetime

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Class that provides simple container for date
 *
 * @author Arseniy Svechkarev
 *
 * @constructor Trying to assign input date to primary date. Success only if
 * input date represents a [DatePattern.STANDARD] e.g `2019-06-23` or `2018-08-01`
 */
class Date(inputDate: String) : Comparable<Date> {
  
  /**
   * Primary date object in string representation
   */
  val stringDate: String
  
  init {
    val formatter = SimpleDateFormat(DatePattern.STANDARD, Locale.US)
    try {
      formatter.parse(inputDate)
    } catch (e: ParseException) {
      throw IllegalArgumentException("Incorrect input date: $inputDate. " +
          "Only dates in pattern \"${DatePattern.STANDARD}\" are allowed")
    }
    stringDate = inputDate
  }
  
  override fun compareTo(other: Date) = stringDate.compareTo(other.stringDate)
}