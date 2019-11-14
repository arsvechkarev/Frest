package com.arsvechkarev.frest.reports.all

import android.content.Context
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.models.reports.ReportsTriple
import com.arsvechkarev.frest.reports.database.converters.convertedProjects
import com.arsvechkarev.frest.reports.database.converters.fillChartValues
import com.arsvechkarev.frest.reports.database.converters.withoutShortDurationPercents
import com.arsvechkarev.frest.reports.database.readers.AllReportsReader
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

/**
 * Presenter for [AllReportsFragment]
 *
 * @author Arseniy Svechkarev
 */
class AllReportsPresenter(
  context: Context,
  
  // Listener for sending callbacks to fragment
  private var allReportsCallbacks: AllReportsEventsCallbacks?
) : ReportsAdapterHeaderCallbacks {
  
  private val reportsAdapter = ProjectsAllReportsAdapter(context, this)
  private var allActivitiesReader = AllReportsReader(context)
  
  // Values for graph
  private var pieEntries = ArrayList<PieEntry>()
  private var colors = ArrayList<Int>()
  
  init {
    GlobalScope.launch(Dispatchers.Main) {
      changeDateRange(StringDateValues.today(), true)
    }
  }
  
  private suspend fun changeDateRange(dateFrom: String, isFirstTime: Boolean) {
    allReportsCallbacks?.onStartLoading()
    val projects = getProjectsAsync(dateFrom)
    updateGraphInfo(projects)
    allReportsCallbacks?.onFinishLoading()
    if (projects.isEmpty()) {
      allReportsCallbacks?.onNoData(reportsAdapter)
    } else {
      if (isFirstTime) {
        allReportsCallbacks?.onPrepareRecycler(reportsAdapter)
      } else {
        allReportsCallbacks?.onDataSetChanged(projects)
      }
    }
  }
  
  private fun updateGraphInfo(projects: List<ReportsTriple>) {
    reportsAdapter.data = projects
    pieEntries.clear()
    colors.clear()
    projects.withoutShortDurationPercents()
        .fillChartValues(pieEntries, colors)
  }
  
  override suspend fun onDataRangeChanged(itemId: Int) = withContext(Dispatchers.Main) {
    when (itemId) {
      R.id.menu_today -> changeDateRange(StringDateValues.today(), false)
      R.id.menu_last_7_days -> changeDateRange(StringDateValues.lastSevenDaysDate(), false)
      R.id.menu_last_30_days -> changeDateRange(StringDateValues.lastThirtyDaysDate(), false)
      R.id.menu_last_365_days -> changeDateRange(StringDateValues.lastYearDate(), false)
      R.id.menu_all_time -> changeDateRange(StringDateValues.ALL_TIME, false)
    }
  }
  
  override val graphEntries: ArrayList<PieEntry>
    get() = pieEntries
  
  override val graphColors: ArrayList<Int>
    get() = colors
  
  private suspend fun getProjectsAsync(date: String): List<ReportsTriple> =
      withContext(Dispatchers.Default) {
        val reportsList = if (date == StringDateValues.ALL_TIME) {
          allActivitiesReader.getAllStats()
        } else {
          allActivitiesReader.getAllStatsFrom(date)
        }
        return@withContext reportsList.convertedProjects()
      }
  
  fun detachView() {
    allReportsCallbacks = null
  }
}