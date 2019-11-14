package com.arsvechkarev.frest.starttask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.Codes.Key
import com.arsvechkarev.frest.additional.Codes.Result
import com.arsvechkarev.frest.additional.utils.getPickerDialogStyleId
import com.arsvechkarev.frest.additional.utils.showNormalToast
import com.arsvechkarev.frest.models.main.Project
import kotlinx.android.synthetic.main.activity_add_record.cardSaveRecord
import kotlinx.android.synthetic.main.activity_add_record.textDate
import kotlinx.android.synthetic.main.activity_add_record.textDuration
import kotlinx.android.synthetic.main.activity_add_record.textProjectName
import kotlinx.android.synthetic.main.activity_add_record.textWarning

/**
 * Activity for adding task time manually
 *
 * @author Arseniy Svechkarev
 */
class AddRecordActivity : AppCompatActivity(), RecordActionsListener {
  
  /** Main presenter */
  private lateinit var presenter: AddRecordPresenter
  
  /**
   * Project that was received from [StartTaskActivity]
   */
  private lateinit var project: Project
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_record)
    project = intent.getParcelableExtra(Key.PROJECT_ADDING_RECORD)
    presenter = AddRecordPresenter(project, this)
    prepareViews()
  }
  
  /**
   * Initializing views by received project properties
   */
  private fun prepareViews() {
    textProjectName.text = project.name
    textProjectName.setTextColor(project.color)
    textDuration.text = presenter.defaultDurationView
    textDate.text = presenter.defaultDateView
  }
  
  @Suppress("UNUSED_PARAMETER")
  fun onDurationClick(clickedView: View) {
    val hours = presenter.durationHours
    val minutes = presenter.durationMinutes
    val dialog = TimePickerDialog(
      this,
      getPickerDialogStyleId(project),
      TimePickerDialog.OnTimeSetListener(presenter::onTimeSet),
      hours,
      minutes,
      true)
    dialog.show()
  }
  
  @Suppress("UNUSED_PARAMETER")
  fun onDateClick(clickedView: View) {
    val year = presenter.dateYear
    val month = presenter.dateMonth - 1
    val day = presenter.dateDay
    val dialog = DatePickerDialog(
      this,
      getPickerDialogStyleId(project),
      DatePickerDialog.OnDateSetListener(presenter::onDateSet),
      year,
      month,
      day)
    dialog.show()
  }
  
  @Suppress("UNUSED_PARAMETER")
  fun onSaveClick(clickedView: View) {
    presenter.onSave()
  }
  
  /**
   * When user changed duration of record
   */
  override fun onDurationChanged(strDuration: CharSequence) {
    textDuration.text = strDuration
  }
  
  /**
   * When user changed date of record
   */
  override fun onDateChanged(strDate: String) {
    textDate.text = strDate
  }
  
  /**
   * When user's selected properties of record fully correct and record can be saved
   */
  override fun onAllowToSave() {
    cardSaveRecord.visibility = VISIBLE
    textWarning.visibility = INVISIBLE
    cardSaveRecord.isEnabled = true
  }
  
  /**
   * When user's selected properties of record are not correct and record can't be saved
   */
  override fun onRejectToSave() {
    cardSaveRecord.visibility = GONE
    textWarning.visibility = VISIBLE
    showNormalToast(this, getString(R.string.error_zero_duration))
  }
  
  /**
   * Saving record
   *
   * @param resultData Intent with task
   */
  override fun onSave(resultData: Intent) {
    setResult(Result.ADD_RECORD_INTERIM, resultData)
    finish()
  }
}
