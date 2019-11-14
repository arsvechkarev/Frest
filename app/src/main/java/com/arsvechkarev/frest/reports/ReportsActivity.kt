package com.arsvechkarev.frest.reports

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.Codes
import com.arsvechkarev.frest.additional.Codes.Key
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.projects.OnProjectClickListener
import com.arsvechkarev.frest.projects.ProjectsListFragment
import com.arsvechkarev.frest.reports.all.AllReportsFragment
import com.arsvechkarev.frest.reports.byproject.SpecProjectReportsActivity
import kotlinx.android.synthetic.main.activity_reports.tabLayout
import kotlinx.android.synthetic.main.activity_reports.viewPager
import kotlinx.android.synthetic.main.partial_toolbar.toolbar

/**
 * Activity contains [AllReportsFragment] and [ProjectsListFragment]
 *
 * @author Arseniy Svechkarev
 */
class ReportsActivity : AppCompatActivity(), OnProjectClickListener {
  
  private var projectsFragment = ProjectsListFragment()
  private var allReportsFragment = AllReportsFragment()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reports)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setupPages()
    defineFragmentToDisplay()
  }
  
  private fun defineFragmentToDisplay() {
    // If this activity started from MainActivity, then AllReportsFragment should be
    // displayed, and if this activity started from SpecProjectReportsActivity (when user
    // clicks "back" or "home" on it), then should be displayed ProjectsListFragment
    val isMainActivity = intent.hasExtra(Codes.Flag.FROM_MAIN_ACTIVITY)
    if (isMainActivity) {
      viewPager.currentItem = 0
    } else {
      viewPager.currentItem = 1
    }
  }
  
  override fun onProjectClick(project: Project) {
    val intent = Intent(this, SpecProjectReportsActivity::class.java)
    intent.putExtra(Key.PROJECT, project as Parcelable)
    startActivity(intent)
  }
  
  private fun setupPages() {
    viewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
    tabLayout.setupWithViewPager(viewPager)
  }
  
  /** Class for view pager */
  private inner class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager)
    : androidx.fragment.app.FragmentPagerAdapter(fm) {
    
    override fun getItem(position: Int): androidx.fragment.app.Fragment {
      if (position == 0) {
        return allReportsFragment
      } else if (position == 1) {
        return projectsFragment
      }
      throw IllegalStateException("Unknown fragment")
    }
    
    override fun getCount(): Int {
      return 2
    }
    
    override fun getPageTitle(position: Int): CharSequence? {
      if (position == 0) {
        return getString(R.string.title_all_activities)
      } else if (position == 1) {
        return getString(R.string.title_by_project)
      }
      throw IllegalStateException("Unknown title")
    }
  }
}
