package com.miyar.app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.miyar.app.data.MiyarDatabase
import java.util.Calendar

class ReminderScheduler {
    companion object {
        const val AFTERNOON_REQ_CODE = 1001
        const val EVENING_REQ_CODE = 1002
        const val CHANNEL_ID = "miyar_smart_reminders"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "تنبيهات ميار الذكية"
                val descriptionText = "إشعارات متابعة الأعمال والأنشطة الزراعية والمالية"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun scheduleDailyReminders(context: Context) {
            createNotificationChannel(context)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // 1. إشعار الساعة 3:00 عصراً (استباقي واقتراح المهام)
            val afternoonIntent = Intent(context, ReminderReceiver::class.java).apply {
                action = "ACTION_AFTERNOON_CHECK"
            }
            val afternoonPending = PendingIntent.getBroadcast(
                context, AFTERNOON_REQ_CODE, afternoonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val afternoonCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 15)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                afternoonCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                afternoonPending
            )

            // 2. إشعار الساعة 8:00 مساءً (تذكير بالتدقيق والتسجيل)
            val eveningIntent = Intent(context, ReminderReceiver::class.java).apply {
                action = "ACTION_EVENING_CHECK"
            }
            val eveningPending = PendingIntent.getBroadcast(
                context, EVENING_REQ_CODE, eveningIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val eveningCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                eveningCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                eveningPending
            )
        }
    }
}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val db = MiyarDatabase.getInstance(context)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            "ACTION_AFTERNOON_CHECK" -> {
                val todayTasksCount = db.getScheduledTasksCountForToday()
                val message = if (todayTasksCount > 0) {
                    "لديك اليوم $todayTasksCount عملية زراعية مقترحة ومجدولة. هل قمت بإنجازها؟"
                } else {
                    "تفقّد مواضعك ومحاصيلك اليوم، وراجع خطتك الزراعية في ميار."
                }
                
                val builder = NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_miyar_launcher)
                    .setContentTitle("متابعة يومك الزراعي من مِعيار")
                    .setContentText(message)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

                notificationManager.notify(ReminderScheduler.AFTERNOON_REQ_CODE, builder.build())
            }

            "ACTION_EVENING_CHECK" -> {
                val hasLoggedToday = db.hasLoggedActivityToday()
                if (!hasLoggedToday) {
                    val message = "لم تسجل أي عملية زراعية أو مالية اليوم في ميار. إذا أنجزت عملاً في أحد مواضعك، سجله الآن حتى تبقى حساباتك وتقاريرك دقيقة."
                    
                    val builder = NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_miyar_launcher)
                        .setContentTitle("متابعة يومك الزراعي من مِعيار")
                        .setContentText(message)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                    notificationManager.notify(ReminderScheduler.EVENING_REQ_CODE, builder.build())
                }
            }
        }
    }
}
