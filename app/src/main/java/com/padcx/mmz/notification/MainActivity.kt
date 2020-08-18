package com.padcx.mmz.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "PADCNOTIFICATION_CHANNEL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("TOKEN", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = "token: $token"
                Log.e("TOKEN", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })


        val notificationId: Int = 1996
        val notificationId02: Int = 1997
        val notificationId03: Int = 1998
        val notificationId04: Int = 1999
        val notificationId05: Int = 2000
        val notificationId06: Int = 2001

        /*Notification Sample*/
        var textTitle = "Hello Friend"
        var textContent = "I am a New Notification!"

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(textTitle)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        button.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }

        /* Notification with Intent*/
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        var builder2 = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        btnNotiWithIntent.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId02, builder2.build())
            }
        }


        /* Notification with Actions*/

        val snoozeIntent = Intent(this,WelcomeActivity::class.java).apply {
            putExtra(Notification.EXTRA_NOTIFICATION_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        val builder3 = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("My notification")
            .setContentText("Hello Action")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
//                .setFullScreenIntent(pendingIntent, true)  // urgent noti type only use
//                .setVisibility(VISIBILITY_PRIVATE)   // visibility modes
            .addAction(android.R.drawable.star_off, "Snooze",
                snoozePendingIntent)

        btnNotiWithActions.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId03, builder3.build())
            }
        }


        /* Notification with Progress*/
        val builder4 = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(android.R.drawable.stat_sys_download)
        }

        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 30
        NotificationManagerCompat.from(this).apply {
            builder4.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            builder.setContentText("Download complete")
                .setProgress(0, 0, false)

            btnProgress.setOnClickListener {
                notify(notificationId04, builder4.build())
            }
        }


        /* Notification with Image*/

        val drawable: Drawable? = ContextCompat.getDrawable(this,R.drawable.day)

        val builder5 = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("imageTitle")
            .setContentText("imageDescription")
            .setLargeIcon(drawable!!.toBitmap())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(drawable.toBitmap())
                .bigLargeIcon(drawable.toBitmap()))  // null

        btnBigImage.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId05, builder5.build())
            }
        }



        /*
      Custom Notification
       */
        val notificationLayout = RemoteViews(packageName, R.layout.notification_layout)

        val customNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
           // .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            // .setCustomContentView(notificationLayout)
              .setCustomBigContentView(notificationLayout)

        btnCustom.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId06, customNotification.build())
            }
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PADC Myanmar"
            val descriptionText = "PADC Learning Portal"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
}