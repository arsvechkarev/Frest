package com.arsvechkarev.frest.additional.datetime

/**
 * Pattern for different representation of dates
 *
 * @author Arseniy Svechkarev
 */
object DatePattern {
  
  /**
   * Standard pattern.
   * If date is "7th June 2019" result is "2019-06-07"
   */
  const val STANDARD = "yyyy-MM-dd"
  
  /**
   * Pretty pattern for timeline
   * If date is `7th June 2019", result is "Friday, Jun 7"
   */
  const val PRETTY = "EEEE, MMM d"
  
  /**
   * Pretty pattern with year for timeline
   * If date is "7th June 2019", result is "Friday, Jun 7, 2019"
   */
  const val PRETTY_YEAR = "EEEE, MMM d, yyyy"
  
  /**
   * Pretty pattern for date picker
   * If date is "7th June 2019", result is "Fri, Jun 7"
   */
  const val FULL = "EEE, MMM d"
  
  /**
   * Pretty pattern with year for date picker
   * If date is "7th June 2019", result is "Fri, Jun 7, 2019"
   */
  const val FULL_YEAR = "EEE, MMM d, yyyy"
  
  /**
   * Pretty pattern for line chart
   * If date is "7th June 2019", result is "Jun 7"
   */
  const val CHART = "MMM d"
  
  /**
   * Pretty pattern for selected value chart with year
   * If date is "7th June 2019", result is "Jun 7, 2019"
   */
  const val CHART_YEAR = "MMM d, yyyy"
}
