package com.glowremind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      GlowRemindApp()
    }
  }
}

data class Reminder(val title: String, val detail: String, val tag: String)

enum class ReminderFrequency(val label: String) {
  HOURLY("Hourly"),
  DAILY("Daily"),
  WEEKLY("Weekly"),
  MONTHLY("Monthly"),
  CUSTOM("Custom")
}

@Composable
fun GlowRemindApp() {
  MaterialTheme {
    Surface {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              listOf(Color(0xFF1B2032), Color(0xFF0B0C13))
            )
          )
      ) {
        val reminders = remember {
          mutableStateListOf(
            Reminder("Stand & stretch", "Hourly • Next at 11:00", "Active"),
            Reminder("Project deep work", "Weekdays • 14:00", "Focus")
          )
        }
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          item {
            HeroSection()
          }
          item {
            FocusStatusCard()
          }
          item {
            ScheduleHighlights()
          }
          item {
            ReminderForm(reminders)
          }
          item {
            UpcomingList(reminders)
          }
          item {
            ProductivityGrid()
          }
          item {
            FooterCallout()
          }
          item { Spacer(modifier = Modifier.height(24.dp)) }
        }
      }
    }
  }
}

@Composable
fun HeroSection() {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = "GlowRemind",
      color = Color(0xFF7CF2FF),
      letterSpacing = 4.sp,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )
    Text(
      text = "Never miss a moment.",
      color = Color.White,
      fontSize = 28.sp,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = "Hourly, daily, weekly, monthly, and custom reminders — all synced to your focus flow.",
      color = Color(0xFFA6ADBB),
      fontSize = 14.sp
    )
    Button(
      onClick = {},
      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CF2FF)),
      shape = RoundedCornerShape(18.dp)
    ) {
      Text("+ New Reminder", color = Color(0xFF0B0C13), fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
fun FocusStatusCard() {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF151A27)),
    shape = RoundedCornerShape(24.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Today’s Focus", color = Color.White, fontWeight = FontWeight.SemiBold)
        Text("12 reminders • 4 habits • 2 smart breaks", color = Color(0xFFA6ADBB), fontSize = 12.sp)
      }
      GlowPill(text = "86% on-time")
    }
  }
}

@Composable
fun GlowPill(text: String) {
  Box(
    modifier = Modifier
      .background(
        brush = Brush.horizontalGradient(
          listOf(Color(0x337CF2FF), Color(0x55B36BFF))
        ),
        shape = RoundedCornerShape(999.dp)
      )
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Text(text, color = Color(0xFF7CF2FF), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
  }
}

@Composable
fun ScheduleHighlights() {
  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      FeatureCard(
        title = "Smart Schedules",
        description = "Hourly check-ins, daily rituals, weekly reviews, monthly goals.",
        tags = listOf("Hourly", "Daily", "Weekly", "Monthly")
      )
    }
    FeatureCard(
      title = "Custom Reminders",
      description = "Stack multiple alerts, silent hours, and location triggers.",
      tags = listOf("Geo", "Stacked", "Silent")
    )
  }
}

@Composable
fun FeatureCard(title: String, description: String, tags: List<String>) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF141A29)),
    shape = RoundedCornerShape(22.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
      Text(description, color = Color(0xFFA6ADBB), fontSize = 12.sp)
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tags.forEach { tag ->
          TagChip(tag)
        }
      }
    }
  }
}

@Composable
fun TagChip(text: String) {
  Box(
    modifier = Modifier
      .background(Color(0xFF0E111A), shape = RoundedCornerShape(999.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Text(text, color = Color(0xFFA6ADBB), fontSize = 11.sp)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderForm(reminders: MutableList<Reminder>) {
  val context = LocalContext.current
  var title by remember { mutableStateOf("") }
  var frequency by remember { mutableStateOf(ReminderFrequency.HOURLY) }
  var time by remember { mutableStateOf("09:00") }
  var style by remember { mutableStateOf("Glow pop") }
  var customMinutes by remember { mutableStateOf("90") }
  var frequencyExpanded by remember { mutableStateOf(false) }

  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF141823)),
    shape = RoundedCornerShape(24.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Create Reminder", color = Color.White, fontWeight = FontWeight.SemiBold)
      OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Title") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = Color(0xFF7CF2FF),
          focusedLabelColor = Color(0xFF7CF2FF),
          unfocusedBorderColor = Color(0xFF2C3242),
          unfocusedLabelColor = Color(0xFFA6ADBB),
          textColor = Color.White
        )
      )
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.weight(1f)) {
          OutlinedTextField(
            value = frequency.label,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Frequency") },
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
              focusedBorderColor = Color(0xFF7CF2FF),
              focusedLabelColor = Color(0xFF7CF2FF),
              unfocusedBorderColor = Color(0xFF2C3242),
              unfocusedLabelColor = Color(0xFFA6ADBB),
              textColor = Color.White
            )
          )
          Box(
            modifier = Modifier
              .matchParentSize()
              .background(Color.Transparent)
              .clickable { frequencyExpanded = true }
          )
          DropdownMenu(expanded = frequencyExpanded, onDismissRequest = { frequencyExpanded = false }) {
            ReminderFrequency.values().forEach { option ->
              DropdownMenuItem(
                text = { Text(option.label) },
                onClick = {
                  frequency = option
                  frequencyExpanded = false
                }
              )
            }
          }
        }
        OutlinedTextField(
          value = time,
          onValueChange = { time = it },
          modifier = Modifier.width(110.dp),
          label = { Text("Time") },
          colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF7CF2FF),
            focusedLabelColor = Color(0xFF7CF2FF),
            unfocusedBorderColor = Color(0xFF2C3242),
            unfocusedLabelColor = Color(0xFFA6ADBB),
            textColor = Color.White
          )
        )
      }
      if (frequency == ReminderFrequency.CUSTOM) {
        OutlinedTextField(
          value = customMinutes,
          onValueChange = { customMinutes = it },
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Custom interval (minutes)") },
          colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF7CF2FF),
            focusedLabelColor = Color(0xFF7CF2FF),
            unfocusedBorderColor = Color(0xFF2C3242),
            unfocusedLabelColor = Color(0xFFA6ADBB),
            textColor = Color.White
          )
        )
      }
      OutlinedTextField(
        value = style,
        onValueChange = { style = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Notification Style") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = Color(0xFF7CF2FF),
          focusedLabelColor = Color(0xFF7CF2FF),
          unfocusedBorderColor = Color(0xFF2C3242),
          unfocusedLabelColor = Color(0xFFA6ADBB),
          textColor = Color.White
        )
      )
      Button(
        onClick = {
          if (title.isNotBlank()) {
            val detail = when (frequency) {
              ReminderFrequency.CUSTOM -> "${frequency.label} • ${customMinutes}m • $style"
              else -> "${frequency.label} • $time • $style"
            }
            reminders.add(0, Reminder(title.trim(), detail, "Active"))
            ReminderScheduler.scheduleReminder(
              context,
              ReminderConfig(
                title = title.trim(),
                frequency = frequency,
                time = time,
                style = style,
                customMinutes = customMinutes.toIntOrNull() ?: 90
              )
            )
            title = ""
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CF2FF)),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("Schedule Reminder", color = Color(0xFF0B0C13), fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun UpcomingList(reminders: List<Reminder>) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF141823)),
    shape = RoundedCornerShape(24.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("Upcoming", color = Color.White, fontWeight = FontWeight.SemiBold)
        TextButton(onClick = {}) {
          Text("View all", color = Color(0xFF7CF2FF))
        }
      }
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        reminders.take(4).forEach { reminder ->
          ReminderRow(reminder)
        }
      }
    }
  }
}

@Composable
fun ReminderRow(reminder: Reminder) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF0F131D), RoundedCornerShape(16.dp))
      .padding(12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(reminder.title, color = Color.White, fontWeight = FontWeight.SemiBold)
      Text(
        reminder.detail,
        color = Color(0xFFA6ADBB),
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
    GlowPill(reminder.tag)
  }
}

@Composable
fun ProductivityGrid() {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF141823)),
    shape = RoundedCornerShape(24.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Power Features", color = Color.White, fontWeight = FontWeight.SemiBold)
      FeatureTile(title = "Noti Stacks", text = "Bundle alerts to reduce distraction and keep momentum.")
      FeatureTile(title = "Priority Lanes", text = "Color-coded urgency with swipe-to-snooze gestures.")
      FeatureTile(title = "Focus Guardians", text = "Auto-mute noisy apps during scheduled work blocks.")
      FeatureTile(title = "Streak Boost", text = "Track habits, streaks, and weekly productivity wins.")
    }
  }
}

@Composable
fun FeatureTile(title: String, text: String) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    Text(text, color = Color(0xFFA6ADBB), fontSize = 12.sp)
  }
}

@Composable
fun FooterCallout() {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF141823)),
    shape = RoundedCornerShape(24.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text("Ready for a glowing day?", color = Color.White, fontWeight = FontWeight.SemiBold)
      Text(
        "Sync across devices, backup to cloud, and export to calendar.",
        color = Color(0xFFA6ADBB),
        fontSize = 12.sp
      )
      Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CF2FF)),
        shape = RoundedCornerShape(18.dp)
      ) {
        Text("Enable Notifications", color = Color(0xFF0B0C13), fontWeight = FontWeight.Bold)
      }
    }
  }
}
