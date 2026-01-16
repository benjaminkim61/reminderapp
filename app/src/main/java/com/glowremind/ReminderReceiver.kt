package com.glowremind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val reminderId = intent.getIntExtra(EXTRA_ID, 0)
    val title = intent.getStringExtra(EXTRA_TITLE) ?: "Reminder"
    val style = intent.getStringExtra(EXTRA_STYLE) ?: "Glow"
    val frequency = intent.getStringExtra(EXTRA_FREQUENCY) ?: "Custom"

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "GlowRemind Alerts",
        NotificationManager.IMPORTANCE_HIGH
      )
      channel.description = "GlowRemind scheduled notifications"
      manager.createNotificationChannel(channel)
    }

    val openIntent = Intent(context, MainActivity::class.java)
    val openPendingIntent = PendingIntent.getActivity(
      context,
      reminderId,
      openIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_popup_reminder)
      .setContentTitle(title)
      .setContentText("$frequency • $style")
      .setAutoCancel(true)
      .setContentIntent(openPendingIntent)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .build()

    manager.notify(reminderId, notification)
    ReminderScheduler.handleReminderFired(context, reminderId)
  }

  companion object {
    const val CHANNEL_ID = "glowremind_channel"
    const val EXTRA_ID = "extra_id"
    const val EXTRA_TITLE = "extra_title"
    const val EXTRA_STYLE = "extra_style"
    const val EXTRA_FREQUENCY = "extra_frequency"
  }
}
