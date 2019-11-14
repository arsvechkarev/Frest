package com.arsvechkarev.frest.reports.all

import com.arsvechkarev.frest.models.reports.ReportsTriple

/**
 * Interface for sending callbacks related to all reports events, like
 * loading data, refreshing etc.
 *
 * @author Arseniy Svechkarev
 */
interface AllReportsEventsCallbacks {
  
  /** When loading from database starts */
  fun onStartLoading()
  
  /**
   * When we have first ready adapter with data set, and it has to be attached
   * to recycler view
   */
  fun onPrepareRecycler(adapter: ProjectsAllReportsAdapter)
  
  /** When reports data changes */
  fun onDataSetChanged(projects: List<ReportsTriple>)
  
  /**
   * When data is empty, so we need to bound adapter with empty data
   * to make card with date ranges still visible
   */
  fun onNoData(adapter: ProjectsAllReportsAdapter)
  
  /** When loading from database finishes */
  fun onFinishLoading()
}
