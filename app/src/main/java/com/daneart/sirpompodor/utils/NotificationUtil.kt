package com.daneart.sirpompodor.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.daneart.sirpompodor.R
import com.daneart.sirpompodor.activities.PompoActivity

class NotificationUtil {
    companion object {

        private const val CHANNEL_ID_TIMER = "pompodoro_timer"
        private const val CHANNEL_NAME_TIMER = "Senioro Pompodoro"
        private const val TIMER_ID = 0

        fun showTimerRunning(
            context: Context,
            processType: String
        ) {

            val openIntent = Intent(context, PompoActivity::class.java)
            val openPendingIntent = PendingIntent.getActivity(
                context, 0, openIntent
                , PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder =
                getBasicNotificationBuilder(
                    context,
                    CHANNEL_ID_TIMER
                )

            builder.setContentTitle(processType)
                .setContentText("Running")
                .setOngoing(true)
                .setContentIntent(openPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            nManager.createNotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER, true
            )

            nManager.notify(TIMER_ID, builder.build())
        }

        fun hideTimerNotification(context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        fun showTimerPaused(context: Context, processType: String) {
            val openIntent = Intent(context, PompoActivity::class.java)
            val openPendingIntent = PendingIntent.getActivity(
                context, 0, openIntent
                , PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder =
                getBasicNotificationBuilder(
                    context,
                    CHANNEL_ID_TIMER
                )
            builder.setContentTitle(processType)
                .setContentText("Paused")
                .setOngoing(true)
                .setContentIntent(openPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            nManager.createNotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER, true
            )

            nManager.notify(TIMER_ID, builder.build())
        }

        fun showTimeExpired(context: Context) {
            val openIntent = Intent(context, PompoActivity::class.java)
            val openPendingIntent = PendingIntent.getActivity(
                context, 0, openIntent
                , PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder =
                getBasicNotificationBuilder(
                    context,
                    CHANNEL_ID_TIMER
                )


            builder.setContentTitle("Time is over!")
                .setContentText("Tap to open Pompodoro.")
                .setOngoing(true)
                .setContentIntent(openPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            nManager.createNotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER, true
            )

            nManager.notify(TIMER_ID, builder.build())
        }

        private fun getBasicNotificationBuilder(
            context: Context,
            channelId: String
        )
                : NotificationCompat.Builder {
            return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(0)
        }

        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(
            channelID: String,
            channelName: String,
            playSound: Boolean
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelID, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }


    }
}