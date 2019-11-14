package com.arsvechkarev.frest.models.reports

import com.arsvechkarev.frest.additional.datetime.Date

/**
 * Pair of date and corresponding duration to display in graph
 */
class DateDurationPair(
  val date: Date, val duration: Long) {
  
  override fun toString(): String {
    return "DateDurationPair: date={${date.stringDate}}, duration={$duration}"
  }
}