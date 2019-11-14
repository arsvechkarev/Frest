package com.arsvechkarev.frest.starttask

import android.content.Intent

/**
 * Interface for communicating [AddRecordPresenter] with [AddRecordActivity]
 *
 * @author Arseniy Svechkarev
 */
interface RecordActionsListener {
  
  /**
   * When user changes duration of TimePicker
   *
   * @see AddRecordPresenter.onTimeSet
   */
  fun onDurationChanged(strDuration: CharSequence)
  
  /**
   * When user changes duration of DatePicker
   *
   * @see AddRecordPresenter.onDateSet
   */
  fun onDateChanged(strDate: String)
  
  /**
   * When entered duration and date is correct and user can save record
   */
  fun onAllowToSave()
  
  /**
   * When entered duration and date isn't correct and record can't be saved
   */
  fun onRejectToSave()
  
  /**
   * When user saves record
   *
   * @param resultData Intent with created task
   * @see AddRecordPresenter.onSave
   */
  fun onSave(resultData: Intent)
  
}
