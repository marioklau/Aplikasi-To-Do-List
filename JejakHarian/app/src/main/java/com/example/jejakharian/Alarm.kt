package com.example.jejakharian

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationCompat


class Alarm : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Buat notifikasi
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1

        // Buat saluran notifikasi untuk Android O dan yang lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("alarm_channel", "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Buat intent untuk aksi ketuk notifikasi
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        // Bangun notifikasi
        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle("Event Reminder")
            .setContentText("You have an event scheduled!")
            .setSmallIcon(R.drawable.baseline_access_time_24) // Ganti dengan ikon aplikasi Anda
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        // Tampilkan notifikasi
        notificationManager.notify(notificationId, notification)
    }
}