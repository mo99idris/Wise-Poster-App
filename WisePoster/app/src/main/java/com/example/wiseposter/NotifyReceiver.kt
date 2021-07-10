package com.example.wiseposter


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build

import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotifyReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    lateinit var notifyRec:Bundle

    override fun onReceive(p0: Context, p1: Intent) {
        notifyRec = p1.extras!!

        createNotificaionChannel(p0)
        sendNotification(p0)

    }

    private fun createNotificaionChannel(p0: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "Notificaion Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager:NotificationManager = p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun sendNotification(p0: Context){

        val intent = Intent(p0, LoginAct::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent:PendingIntent = PendingIntent.getActivity(p0, 0, intent, 0)



        val builder = NotificationCompat.Builder(p0, CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_campaign_black_20)
            .setContentTitle("Wise Poster ")
            .setContentText(notifyRec.getString("msg"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(p0)){
            notify(notificationId, builder.build())
        }
    }
}





