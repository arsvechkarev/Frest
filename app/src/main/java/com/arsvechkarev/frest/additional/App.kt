package com.arsvechkarev.frest.additional

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import com.arsvechkarev.frest.projects.ProjectsListFragment
import com.arsvechkarev.frest.session.TimerService

/**
 * Main application class (Singleton)
 *
 * @author Arseniy Svechkarev
 */
class App : Application() {
  
  override fun onCreate() {
    super.onCreate()
    instance = this
    screenHeight = resources.displayMetrics.heightPixels
    createTimerNotificationChannel()
  }
  
  /**
   * Creating foreground time counting notification channel
   */
  private fun createTimerNotificationChannel() {
    if (SDK_INT >= O) {
      val serviceTimerChannel = NotificationChannel(TIMER_CHANNEL_ID,
        TIMER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
      serviceTimerChannel.setSound(null, null)
      val manager = getSystemService(NotificationManager::class.java)!!
      manager.createNotificationChannel(serviceTimerChannel)
    }
  }
  
  companion object {
    
    /**
     * Channel name for notification in [TimerService]
     */
    const val TIMER_CHANNEL_NAME = "Foreground time counting"
    
    /**
     * Channel id for notification in [TimerService]
     */
    const val TIMER_CHANNEL_ID = "com.arsvechkarev.frest.foregroundTimeCounting"
    
    // Instance of this singleton
    private lateinit var instance: App
    
    /** Context of Application */
    val context: Context
      get() = instance.applicationContext
    
    /**
     * Actual height of screen. Uses to scroll recycler view with project to
     * top.
     *
     * @see ProjectsListFragment.updateRecycler
     */
    var screenHeight: Int = 0
      private set
  }
}
