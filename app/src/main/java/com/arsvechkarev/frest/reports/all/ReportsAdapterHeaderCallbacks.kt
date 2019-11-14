package com.arsvechkarev.frest.reports.all

import com.github.mikephil.charting.data.PieEntry
import java.util.ArrayList

/**
 * Interface for communication with [ProjectsAllReportsAdapter] and [AllReportsPresenter]
 * It provides methods and properties which need for header item with pie chart in recycler view,
 * like receiving entries & colors, and changing date range
 *
 * @author Arseniy Svechkarev
 */
interface ReportsAdapterHeaderCallbacks {
  
  /** List with entries of pie chart */
  val graphEntries: ArrayList<PieEntry>
  
  /** List with colors related to [graphEntries] */
  val graphColors: ArrayList<Int>
  
  /**
   * Function that invokes when user change date range of reports he want to see. It is
   * suspend to ensure correct order between notifying presenter that data has been changed and
   * getting result ([graphEntries] and [graphColors]) from presenter
   */
  suspend fun onDataRangeChanged(itemId: Int)
}
