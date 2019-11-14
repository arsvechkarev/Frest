package com.arsvechkarev.frest.reports.all

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.myviews.CancelableLayoutManager
import com.arsvechkarev.frest.models.reports.ReportsTriple
import kotlinx.android.synthetic.main.fragment_all_reports.dateRangesStub
import kotlinx.android.synthetic.main.fragment_all_reports.noReportsStub
import kotlinx.android.synthetic.main.fragment_all_reports.progressBar
import kotlinx.android.synthetic.main.fragment_all_reports.recyclerReportsProjects
import kotlinx.android.synthetic.main.stub_template.stubDescription
import kotlinx.android.synthetic.main.stub_template.stubImage
import kotlinx.android.synthetic.main.stub_template.stubTitle

/**
 * Fragment with all reports by different date ranges
 *
 * @author Arseniy Svechkarev
 *
 * @see AllReportsPresenter
 * @see ProjectsAllReportsAdapter
 */
class AllReportsFragment : androidx.fragment.app.Fragment(), AllReportsEventsCallbacks {
  
  private lateinit var myContext: Context
  
  /**
   * This property nullable because in [onStartLoading] all views except progress bar becomes invisible
   * and recycler view data should be empty. To ensure this, adapter of recycler updates. But at
   * this time the adapter can be null, because [onPrepareRecycler] can be not called. In this case,
   * update should not happen
   *
   * TODO (18.10.2019): I don't know what the author meant with this text(
   */
  private var adapter: ProjectsAllReportsAdapter? = null
  
  private lateinit var cancelableLayoutManager: CancelableLayoutManager
  private lateinit var presenter: AllReportsPresenter
  
  override fun onAttach(context: Context?) {
    super.onAttach(context)
    this.myContext = activity as Context
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_all_reports, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter = AllReportsPresenter(myContext, this)
    cancelableLayoutManager = CancelableLayoutManager(myContext)
    recyclerReportsProjects.layoutManager = cancelableLayoutManager
    prepareNoDataStub()
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    presenter.detachView()
  }
  
  /** When loading data from database starts */
  override fun onStartLoading() {
    /* This is date ranges stub, part of 'partial_reports_all_header', which used for displaying
    card when loading starts first time (automatically). This trick uses because if retrieving from
    database is long, user can click on button several times and loading forces again and again. To
    fix this, card with date ranges should be blocked during loading, and it can be easily
    implemented in view holder with reports header (see ProjectsAllReportsAdapter), but at first
    loading, we cannot manage this thing in adapter. So, the solution is create stub which is
    blocked by default, when recycler hasn't been prepared and becomes invisible when first loading
    completed ¯\_(ツ)_/¯ */
    dateRangesStub.isEnabled = false
    
    progressBar.visibility = VISIBLE
    noReportsStub.visibility = GONE
    cancelableLayoutManager.isScrollEnabled = false
    adapter?.data = emptyList()
    adapter?.notifyDataSetChanged()
  }
  
  override fun onPrepareRecycler(adapter: ProjectsAllReportsAdapter) {
    boundAdapter(adapter)
  }
  
  override fun onDataSetChanged(projects: List<ReportsTriple>) {
    adapter?.data = projects
    adapter?.notifyDataSetChanged()
  }
  
  override fun onNoData(adapter: ProjectsAllReportsAdapter) {
    // If adapter is null, only in this case
    // attaching it to recycler
    if (recyclerReportsProjects.adapter == null) {
      boundAdapter(adapter)
    }
    this.adapter?.notifyDataSetChanged()
    noReportsStub.visibility = VISIBLE
    cancelableLayoutManager.isScrollEnabled = false
  }
  
  override fun onFinishLoading() {
    progressBar.visibility = GONE
    dateRangesStub.visibility = GONE
    noReportsStub.visibility = GONE
    cancelableLayoutManager.isScrollEnabled = true
  }
  
  private fun prepareNoDataStub() {
    stubImage.setImageDrawable(ContextCompat.getDrawable(myContext,
      R.drawable.no_all_reports_image))
    stubTitle.text = getString(R.string.text_no_reports_title)
    stubDescription.text = getString(R.string.text_no_reports_description)
  }
  
  private fun boundAdapter(adapter: ProjectsAllReportsAdapter) {
    this.adapter = adapter
    recyclerReportsProjects.adapter = adapter
  }
}
