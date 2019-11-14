package com.arsvechkarev.frest.reports.database.converters

import com.arsvechkarev.frest.additional.datetime.Date
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.additional.datetime.addDay
import com.arsvechkarev.frest.additional.datetime.changeDaysBy
import com.arsvechkarev.frest.additional.datetime.toHour
import com.arsvechkarev.frest.additional.datetime.toNumber
import com.arsvechkarev.frest.additional.utils.lastTrackedDate
import com.arsvechkarev.frest.models.reports.DateDurationPair
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Replenishing missing dates from database with additional dates and
 * default durations (default duration is 0). See [replenishByOtherDates]
 * and [getFilledList] for more info
 */
suspend fun getSpecReportsPairAsync(
  dateDurationsList: List<DateDurationPair>, dateFrom: String
): SpecReportsPair = withContext(Dispatchers.Default) {
  var replenishedList = replenishByOtherDates(dateDurationsList, dateFrom)
  if (dateFrom == StringDateValues.ALL_TIME) {
    replenishedList = replenishedList.toMutableList().addFirstDate()
  }
  val entries = ArrayList<Entry>()
  replenishedList.forEach {
    entries.add(Entry(it.date.toNumber(), it.duration.toHour()))
  }
  return@withContext SpecReportsPair(replenishedList, entries)
}

/*
 * Replenishes list by dates, that might not be in database
 * Example:
 *                                     Date            Duration
 *    Input list from database:     2019-06-12           862
 *                                  2019-06-13           600
 *                                  2019-06-15           30
 *
 *   And if we need dates from "2019-06-11" to "2019-06-16", then output will be:
 *
 *                                     Date            Duration
 *                                  2019-06-11           0
 *                                  2019-06-12           862
 *                                  2019-06-13           600
 *                                  2019-06-14           0
 *                                  2019-06-15           30
 *                                  2019-06-16           0
 */
private fun replenishByOtherDates(
  list: List<DateDurationPair>, dateFrom: String
): List<DateDurationPair> {
  val datesPair = getStartDates(list, dateFrom)
  val currDate = datesPair.first
  val dateTo = datesPair.second
  return getFilledList(currDate, dateTo, list)
}

private fun getStartDates(
  list: List<DateDurationPair>, dateFrom: String
): Pair<String, String> {
  val currDate: String
  val dateTo: String
  if (dateFrom == StringDateValues.ALL_TIME) {
    currDate = list[0].date.stringDate
    dateTo = list.lastTrackedDate()
  } else {
    currDate = dateFrom
    dateTo = StringDateValues.today()
  }
  return Pair(currDate, dateTo)
}

private fun getFilledList(currDate: String, dateTo: String,
                          list: List<DateDurationPair>
): List<DateDurationPair> {
  var copyCurrDate = currDate
  val resultList = ArrayList<DateDurationPair>()
  var existCounter = 0
  val endDate = dateTo.addDay()
  while (copyCurrDate != endDate) {
    if (list.size != existCounter
        && copyCurrDate == list[existCounter].date.stringDate) {
      resultList.add(list[existCounter])
      existCounter++
    } else {
      resultList.add(
        DateDurationPair(Date(copyCurrDate), 0L))
    }
    copyCurrDate = copyCurrDate.addDay()
  }
  return resultList
}

/** Adding date to start of entry list for better view in graph */
private fun MutableList<DateDurationPair>.addFirstDate(): MutableList<DateDurationPair> {
  return this.apply {
    add(0, DateDurationPair(
      Date(this[0].date.stringDate.changeDaysBy(-1)), 0L))
  }
}

/**
 * Class for storing pair with list of [DateDurationPair] and line chart entries
 */
class SpecReportsPair(val projectsList: List<DateDurationPair>, val entries: List<Entry>)
