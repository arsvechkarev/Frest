package com.arsvechkarev.frest.session

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.App.Companion.TIMER_CHANNEL_ID
import com.arsvechkarev.frest.additional.datetime.toStandardTime
import com.arsvechkarev.frest.home.views.MainActivity
import com.arsvechkarev.frest.models.main.Project
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Service for current session foreground notification
 *
 * @author Arseniy Svechkarev
 */
class TimerService : Service() {
  
  companion object {
    const val NOTIFICATION_SERVICE_ID = 314
  }
  
  /** Pending intent for notification */
  private lateinit var pendingIntent: PendingIntent
  
  /** Project to display name in notification */
  private var currentProject: Project? = null
  
  override fun onCreate() {
    super.onCreate()
    EventBus.getDefault().register(this)
    val notificationIntent = Intent(this, MainActivity::class.java)
    pendingIntent = PendingIntent.getActivity(this, 0,
      notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
  }
  
  override fun onBind(intent: Intent?): IBinder? {
    return null
  }
  
  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    currentProject = Stopwatch.getInstance().currentProject
    if (currentProject != null) {
      startForeground(NOTIFICATION_SERVICE_ID, getNotification(Duration(0L)))
    }
    return super.onStartCommand(intent, flags, startId)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    stopForeground(true)
    EventBus.getDefault().unregister(this)
  }
  
  /**
   * Event bus subscriber method. Uses to displaying current time. Time controls from
   * [SessionController]
   *
   * @param duration Current counting time in seconds
   */
  @Subscribe
  @Keep
  fun onCurrentTimeEvent(duration: Duration) {
    if (currentProject != null) {
      val notification = getNotification(duration)
      startForeground(NOTIFICATION_SERVICE_ID, notification)
    }
  }
  
  private fun getNotification(duration: Duration): Notification {
    return Builder(this, TIMER_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_timer)
        .setContentTitle(currentProject!!.name)
        .setContentText(duration.seconds.toStandardTime())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setOnlyAlertOnce(true)
        .build()
  }
}
