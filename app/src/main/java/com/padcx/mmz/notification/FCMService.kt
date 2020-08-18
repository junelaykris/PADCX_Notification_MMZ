package com.padcx.mmz.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants.MessageNotificationKeys.COLOR
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Myint Myint Zaw on 8/18/2020.
 */
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val body=remoteMessage.data["body"]
        val title=remoteMessage.data["title"]

        showNotification(title,body)

    }

    private fun showNotification(title: String?, body: String?) {
       val intent=Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId="HELLO_FCM_CHANNEL"
        val channelName="HELLO_FCM_CHANNEL_HI"
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            setUpNotificationChannels(channelId, channelName, notificationManager)
        }

        val soundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder= NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0,notificationBuilder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpNotificationChannels(channelId: String, channelName: String, notificationManager: NotificationManager) {
        val channel= NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_LOW)
        channel.enableLights(true)
        channel.lightColor= Color.GREEN
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
    }
}