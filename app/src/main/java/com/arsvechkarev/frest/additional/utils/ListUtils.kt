package com.arsvechkarev.frest.additional.utils

import com.arsvechkarev.frest.models.reports.DateDurationPair

fun List<DateDurationPair>.lastTrackedDate(): String {
  val tempList = reversed()
  var date = tempList[0].date.stringDate
  for (pair in tempList) {
    if (pair.duration != 0L) {
      date = pair.date.stringDate
      break
    }
  }
  return date
}

fun List<DateDurationPair>.sumDurations(): Long {
  var sum = 0L
  forEach {
    sum += it.duration
  }
  return sum
}