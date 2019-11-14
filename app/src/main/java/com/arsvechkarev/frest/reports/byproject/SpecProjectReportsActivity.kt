package com.arsvechkarev.frest.reports.byproject

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.Codes
import com.arsvechkarev.frest.additional.datetime.toChartDate
import com.arsvechkarev.frest.additional.datetime.toFullChartDate
import com.arsvechkarev.frest.additional.datetime.toPrettyTime
import com.arsvechkarev.frest.additional.datetime.toSeconds
import com.arsvechkarev.frest.additional.datetime.toStandardTime
import com.arsvechkarev.frest.additional.utils.onValueSelected
import com.arsvechkarev.frest.additional.utils.sumDurations
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.reports.DateDurationPair
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_spec_project_reports.noReportsStub
import kotlinx.android.synthetic.main.activity_spec_project_reports.progressBar
import kotlinx.android.synthetic.main.activity_spec_project_reports.reportsViews
import kotlinx.android.synthetic.main.activity_spec_project_reports.statsLineChart
import kotlinx.android.synthetic.main.activity_spec_project_reports.textCurrentSelection
import kotlinx.android.synthetic.main.activity_spec_project_reports.textDuration
import kotlinx.android.synthetic.main.partial_popup_dates.cardDateRanges
import kotlinx.android.synthetic.main.partial_popup_dates.textDateRanges
import kotlinx.android.synthetic.main.stub_template.stubDescription
import kotlinx.android.synthetic.main.stub_template.stubImage
import kotlinx.android.synthetic.main.stub_template.stubTitle

/**
 * Activity with reports by specific project
 *
 * @author Arseniy Svechkarev
 */
class SpecProjectReportsActivity : AppCompatActivity(),
  SpecProjectReportsCallbacks {
  
  /** Presenter for this activity */
  private lateinit var presenter: SpecProjectReportsPresenter
  
  /** Project, which stats need to displaying */
  private lateinit var project: Project
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_spec_project_reports)
    project = intent.getParcelableExtra(Codes.Key.PROJECT)
    supportActionBar?.title = HtmlCompat.fromHtml("<font color='${project.color}'>" +
        "${project.name}</font>", FROM_HTML_MODE_LEGACY)
    presenter = SpecProjectReportsPresenter(this, this, project)
    presenter.attachView()
    prepareLayout(project)
    prepareNoDataStub()
    setupMenuDateRanges()
    setLineChartListener()
    textDateRanges.text = getString(R.string.text_last_7_days)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    presenter.detachView()
  }
  
  override fun onStartLoading() {
    reportsViews.visibility = GONE
    progressBar.visibility = VISIBLE
    noReportsStub.visibility = GONE
    cardDateRanges.isEnabled = false
  }
  
  override fun onDataUpdated(list: List<DateDurationPair>, entries: List<Entry>) {
    statsLineChart.update(entries.toMutableList(), project.color)
    textDuration.text = list.sumDurations().toPrettyTime(this)
    reportsViews.visibility = VISIBLE
    // Set selection with last entry
    textCurrentSelection.text = getString(R.string.text_spec_project_selection,
      entries.last().x.toChartDate(),
      entries.last().y.toSeconds().toStandardTime())
  }
  
  override fun onNoData() {
    reportsViews.visibility = GONE
    noReportsStub.visibility = VISIBLE
  }
  
  override fun onFinishLoading() {
    progressBar.visibility = GONE
    cardDateRanges.isEnabled = true
    noReportsStub.visibility = GONE
  }
  
  private fun prepareLayout(project: Project) {
    textDuration.setTextColor(project.color)
    progressBar.indeterminateDrawable.setColorFilter(
      project.color, android.graphics.PorterDuff.Mode.SRC_IN)
  }
  
  private fun prepareNoDataStub() {
    stubImage.setImageDrawable(ContextCompat.getDrawable(this,
      R.drawable.no_reports_by_project_image))
    stubTitle.text = getString(R.string.text_no_reports_title)
    stubDescription.text = getString(R.string.text_no_reports_description)
  }
  
  private fun setupMenuDateRanges() {
    val popupMenu = PopupMenu(this, textDateRanges)
    popupMenu.inflate(R.menu.date_ranges)
    popupMenu.setOnMenuItemClickListener { item ->
      textDateRanges.text = item.title
      presenter.requestDateChange(item.itemId)
      true
    }
    val menuItem = popupMenu.menu.findItem(R.id.menu_today)
    menuItem.isVisible = false
    cardDateRanges.setOnClickListener {
      popupMenu.show()
    }
  }
  
  private fun setLineChartListener() {
    statsLineChart.onValueSelected {
      textCurrentSelection.text = getString(R.string.text_spec_project_selection,
        it.chartDateNum.toFullChartDate(), it.duration.toSeconds().toStandardTime())
    }
  }
}
