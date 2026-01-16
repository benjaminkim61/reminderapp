package com.glowremind

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.max
import kotlin.random.Random

private const val PREFS_NAME = "reminder_storage"
private const val PREFS_KEY = "reminder_configs"


data class ReminderConfig(
  val id: Int = Random.nextInt(100000, 999999),
  val title: String,
  val frequency: ReminderFrequency,
  val time: String,
  val style: String,
  val customMinutes: Int
)

object ReminderScheduler {
  fun scheduleReminder(context: Context, config: ReminderConfig) {
    val updated = loadConfigs(context).filterNot { it.id == config.id } + config
    saveConfigs(context, updated)
    scheduleNext(context, config)
  }

  fun rescheduleAll(context: Context) {
    loadConfigs(context).forEach { scheduleNext(context, it) }
  }

  fun handleReminderFired(context: Context, reminderId: Int) {
    val config = loadConfigs(context).firstOrNull { it.id == reminderId } ?: return
    scheduleNext(context, config)
  }

  private fun scheduleNext(context: Context, config: ReminderConfig) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAt = nextTriggerMillis(config)
    val intent = Intent(context, ReminderReceiver::class.java).apply {
      putExtra(ReminderReceiver.EXTRA_ID, config.id)
      putExtra(ReminderReceiver.EXTRA_TITLE, config.title)
      putExtra(ReminderReceiver.EXTRA_STYLE, config.style)
      putExtra(ReminderReceiver.EXTRA_FREQUENCY, config.frequency.label)
    }
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      config.id,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
  }

  private fun nextTriggerMillis(config: ReminderConfig): Long {
    val now = LocalDateTime.now()
    val timeParts = runCatching { LocalDateTime.parse("${now.toLocalDate()}T${config.time}") }
      .getOrElse {
        now.withHour(9).withMinute(0)
      }
    val base = when (config.frequency) {
      ReminderFrequency.HOURLY -> now.plusHours(1)
      ReminderFrequency.DAILY -> {
        if (timeParts.isAfter(now)) timeParts else timeParts.plusDays(1)
      }
      ReminderFrequency.WEEKLY -> {
        if (timeParts.isAfter(now)) timeParts else timeParts.plusWeeks(1)
      }
      ReminderFrequency.MONTHLY -> {
        if (timeParts.isAfter(now)) timeParts else timeParts.plusMonths(1)
      }
      ReminderFrequency.CUSTOM -> now.plusMinutes(max(config.customMinutes, 5).toLong())
    }
    return base.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
  }
}

private fun loadConfigs(context: Context): List<ReminderConfig> {
  val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  val raw = prefs.getString(PREFS_KEY, "[]") ?: "[]"
  val array = JSONArray(raw)
  return List(array.length()) { index ->
    val item = array.getJSONObject(index)
    ReminderConfig(
      id = item.getInt("id"),
      title = item.getString("title"),
      frequency = ReminderFrequency.valueOf(item.getString("frequency")),
      time = item.getString("time"),
      style = item.getString("style"),
      customMinutes = item.getInt("customMinutes")
    )
  }
}

private fun saveConfigs(context: Context, configs: List<ReminderConfig>) {
  val array = JSONArray()
  configs.forEach { config ->
    val item = JSONObject()
    item.put("id", config.id)
    item.put("title", config.title)
    item.put("frequency", config.frequency.name)
    item.put("time", config.time)
    item.put("style", config.style)
    item.put("customMinutes", config.customMinutes)
    array.put(item)
  }
  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    .edit()
    .putString(PREFS_KEY, array.toString())
    .apply()
}
