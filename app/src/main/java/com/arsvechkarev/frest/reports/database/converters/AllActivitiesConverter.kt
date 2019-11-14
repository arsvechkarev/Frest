package com.arsvechkarev.frest.reports.database.converters

import com.arsvechkarev.frest.additional.utils.reduceIfLong
import com.arsvechkarev.frest.models.reports.ReportsTriple
import com.github.mikephil.charting.data.PieEntry

/**
 * Min percent of project duration (min percent calculates relative to sum of all projects
 * durations for selected period). If projects percent is this or more, it should be displayed
 * both in recycler view and pie chart, otherwise, if percent is too small, project should not
 * displaying in pie chart
 */
private const val minPercentToDisplayInChart = 2

fun List<ReportsTriple>.convertedProjects(): List<ReportsTriple> {
  return this
      .removeZeroProject()
      .setProjectPercents()
      .sorted()
      .reversed()
  
}

fun List<ReportsTriple>.withoutShortDurationPercents(): List<ReportsTriple> =
    filter { it.percent >= minPercentToDisplayInChart }


fun List<ReportsTriple>.fillChartValues(pieEntries: ArrayList<PieEntry>, colors: ArrayList<Int>) {
  forEach {
    pieEntries.add(
      PieEntry(it.duration.toFloat(), it.project.name.reduceIfLong())
    )
    colors.add(it.project.color)
  }
}

private fun List<ReportsTriple>.removeZeroProject(): List<ReportsTriple> =
    filter { it.duration > 0 }


private fun List<ReportsTriple>.setProjectPercents(): List<ReportsTriple> {
  val sumDuration = this.sumByDouble { it.duration.toDouble() }
  for (triple in this) {
    triple.percent = (triple.duration / sumDuration * 100)
  }
  return this
}

