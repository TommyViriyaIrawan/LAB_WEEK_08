package com.example.lab_week_08

import android.app.*
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SecondNotificationService : Service() {

    private lateinit var builder: NotificationCompat.Builder
    private lateinit var handler: Handler

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        builder = createForeground()
        val thread = HandlerThread("SecondNotifThread").apply { start() }
        handler = Handler(thread.looper)
    }

    private fun createForeground(): NotificationCompat.Builder {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "SECOND_SERVICE"
        createChannel(channelId)

        val notif = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Final Foreground Service Running")
            .setContentText("Preparing final result…")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        startForeground(SECOND_NOTIF_ID, notif.build())
        return notif
    }

    private fun createChannel(id: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                id, "Second Foreground Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = ContextCompat.getSystemService(
                this, NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val returnValue = super.onStartCommand(intent, flags, startId)

        handler.post {
            for (i in 5 downTo 0) {
                Thread.sleep(1000L)
                builder.setContentText("$i seconds until finish")
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                    .notify(SECOND_NOTIF_ID, builder.build())
            }

            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        return returnValue
    }

    companion object {
        const val SECOND_NOTIF_ID = 0xBA5
    }
}
