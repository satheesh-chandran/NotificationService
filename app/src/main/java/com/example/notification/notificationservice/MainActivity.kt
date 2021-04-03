package com.example.notification.notificationservice

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.content.IntentFilter
import android.os.Build

class MainActivity : AppCompatActivity() {
    private var notificationId: Int = 1
    private val channelId = "CHANNEL_111"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        this.registerNotificationReceiver()
    }

    private fun registerNotificationReceiver() {
        val notificationReceiver = NotificationReceiver()
        val filter = IntentFilter()
        filter.addAction("com.example.notification.NOTIFICATION_LISTENER_SERVICE_EXAMPLE")
        this.baseContext.registerReceiver(notificationReceiver, filter)
        this.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(channelId, getString(R.string.notification_channel), importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            channel.description = getString(R.string.channel_description)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, AlertActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText("Hello boss...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(this.createPendingIntent())
    }

    fun createNotification(v: View) {
        val builder = this.createNotificationBuilder()
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId++, builder.build())
    }

    private inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                intent.getStringExtra("MESSAGE")?.let { Log.d("Satheesh-chandran", it) }
            }
        }
    }
}