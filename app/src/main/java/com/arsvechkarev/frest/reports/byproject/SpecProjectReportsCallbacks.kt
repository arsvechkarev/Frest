package com.arsvechkarev.frest.reports.byproject

import com.arsvechkarev.frest.models.reports.DateDurationPair
import com.github.mikephil.charting.data.Entry

/**
 * Interface for sending callbacks related to specific reports events, like
 * loading data, refreshing etc.
 *
 * @author Arseniy Svechkarev
 */
interface SpecProjectReportsCallbacks {
  
  /** When loading from database starts */
  fun onStartLoading()
  
  /** When data from database updates and we have successful result */
  fun onDataUpdated(list: List<DateDurationPair>, entries: List<Entry>)
  
  /** Where is no data for request */
  fun onNoData()
  
  /** When loading from database finishes */
  fun onFinishLoading()
}