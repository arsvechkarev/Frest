package com.arsvechkarev.frest.models.reports

import com.arsvechkarev.frest.additional.utils.toPrettyString
import com.arsvechkarev.frest.models.main.Project

/**
 * Class for storing project, duration, percent which retrieves from database when
 * user want to see stats of all projects
 *
 * @author Arseniy Svechkarev
 */
data class ReportsTriple(
  /**
   * Project which was retrieved
   */
  val project: Project,
  
  /**
   * Duration in seconds by retrieved dates
   */
  var duration: Long,
  
  /**
   * Percent of particular project duration relative to all projects duration. Sets
   * after project retrieved from database
   */
  var percent: Double = 0.0
) : Comparable<ReportsTriple> {
  
  fun formattedPercent() = percent.toPrettyString()
  
  override fun compareTo(other: ReportsTriple): Int {
    return this.duration.compareTo(other.duration)
  }
}