package com.arsvechkarev.frest.home.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.Codes
import com.arsvechkarev.frest.additional.Codes.Key
import com.arsvechkarev.frest.additional.Codes.Request
import com.arsvechkarev.frest.additional.Codes.Result
import com.arsvechkarev.frest.home.managers.ActivityActionsListener
import com.arsvechkarev.frest.models.main.Project
import com.arsvechkarev.frest.models.main.Task
import com.arsvechkarev.frest.other.AboutActivity
import com.arsvechkarev.frest.reports.ReportsActivity
import com.arsvechkarev.frest.starttask.StartTaskActivity
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.layoutDrawer
import kotlinx.android.synthetic.main.activity_main.navigationView
import kotlinx.android.synthetic.main.partial_toolbar.toolbar

/**
 * Main activity in application
 *
 * @author Arseniy Svechkarev
 */
class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener,
  ActivityActionsListener {
  
  /** Fragment for timeline */
  private lateinit var homeFragment: HomeFragment
  
  private lateinit var intentReports: Intent
  private lateinit var intentAbout: Intent
  private lateinit var intentFeedback: Intent
  private lateinit var intentBuilderShare: ShareCompat.IntentBuilder
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    title = getString(R.string.title_timeline)
    homeFragment = HomeFragment()
    goToHomeFragment()
    initAll()
  }
  
  private fun initAll() {
    setSupportActionBar(toolbar)
    prepareIntents()
    setupDrawer(toolbar)
    setupNavigation()
  }
  
  override fun onResume() {
    super.onResume()
    navigationView.setCheckedItem(R.id.menu_home)
  }
  
  override fun onBackPressed() {
    if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
      layoutDrawer.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }
  
  /** @see ActivityActionsListener.startActivityForResult */
  override fun startActivityForResult() {
    val intent = Intent(this, StartTaskActivity::class.java)
    startActivityForResult(intent, Request.MAIN)
  }
  
  /** @see ActivityActionsListener.startSessionService */
  override fun startSessionService(intentService: Intent) {
    startService(intentService)
  }
  
  /** @see ActivityActionsListener.stopSessionService */
  override fun stopSessionService(serviceIntent: Intent) {
    stopService(serviceIntent)
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Result.START_SESSION) {
      val project = data!!.getParcelableExtra<Project>(Key.PROJECT)
      homeFragment.startSession(project)
    } else if (resultCode == Result.ADD_RECORD) {
      val task = data!!.getParcelableExtra<Task>(Key.TASK)
      homeFragment.manualAddTask(task)
    }
  }
  
  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Delay for drawer (otherwise there can be a lot of lags
    // when large fragments and activities load)
    val drawerDelay: Long = 270
    Handler().postDelayed({
      when (item.itemId) {
        R.id.menu_reports -> startActivity(intentReports)
        R.id.menu_share -> intentBuilderShare.startChooser()
        R.id.menu_send_help_feedback -> startActivity(intentFeedback)
        R.id.menu_about -> startActivity(intentAbout)
      }
    }, drawerDelay)
    layoutDrawer.closeDrawer(GravityCompat.START)
    return true
  }
  
  private fun prepareIntents() {
    intentReports = Intent(this, ReportsActivity::class.java)
    intentReports.putExtra(Codes.Flag.FROM_MAIN_ACTIVITY, Codes.Flag.FROM_MAIN_ACTIVITY)
    intentAbout = Intent(this, AboutActivity::class.java)
    intentFeedback = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_mail)))
    intentBuilderShare = ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setChooserTitle(getString(R.string.text_share_title))
        .setText(getString(R.string.link_share, packageName))
  }
  
  private fun setupNavigation() {
    navigationView.setNavigationItemSelectedListener(this)
    navigationView.setCheckedItem(R.id.menu_home)
  }
  
  private fun setupDrawer(toolbar: Toolbar) {
    val toggle = ActionBarDrawerToggle(this, layoutDrawer, toolbar,
      R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    layoutDrawer.addDrawerListener(toggle)
    toggle.syncState()
  }
  
  private fun goToHomeFragment() {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_main_container, homeFragment).commit()
  }
}
