package com.arsvechkarev.frest.reports.byproject

import android.content.Context
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.datetime.StringDateValues
import com.arsvechkarev.frest.additional.utils.sumDurations
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.projects.ProjectsManager
import com.arsvechkarev.frest.reports.database.converters.getSpecReportsPairAsync
import com.arsvechkarev.frest.reports.database.readers.SpecProjectReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Presenter for [SpecProjectReportsActivity]
 *
 * @author Arseniy Svechkarev
 */
class SpecProjectReportsPresenter(
  private var context: Context?,
  private var reportCallBacks: SpecProjectReportsCallbacks?,
  private var project: Project?
) {
  
  private val specProjectReader = SpecProjectReader(context!!)
  
  fun attachView() {
    changeDateRange(StringDateValues.lastSevenDaysDate())
  }
  
  fun requestDateChange(itemId: Int) {
    when (itemId) {
      R.id.menu_last_7_days -> changeDateRange(StringDateValues.lastSevenDaysDate())
      R.id.menu_last_30_days -> changeDateRange(StringDateValues.lastThirtyDaysDate())
      R.id.menu_last_365_days -> changeDateRange(StringDateValues.lastYearDate())
      R.id.menu_all_time -> changeDateRange(StringDateValues.ALL_TIME)
    }
  }
  
  private fun changeDateRange(dateFrom: String) {
    GlobalScope.launch(Dispatchers.Main) {
      reportCallBacks?.onStartLoading()
      val projectManager = ProjectsManager(context!!)
      val serialCode = projectManager.getProjectSerialCode(project!!)
      val dateDurationsList = specProjectReader.getProjectStats(serialCode, dateFrom)
      if (dateDurationsList.isNotEmpty() and (dateDurationsList.sumDurations() != 0L)) {
        val reportsPair = getSpecReportsPairAsync(dateDurationsList, dateFrom)
        reportCallBacks?.onFinishLoading()
        reportCallBacks?.onDataUpdated(reportsPair.projectsList, reportsPair.entries)
      } else {
        reportCallBacks?.onFinishLoading()
        reportCallBacks?.onNoData()
      }
    }
  }
  
  fun detachView() {
    context = null
    reportCallBacks = null
    project = null
  }
}