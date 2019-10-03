package com.daneart.sirpompodor.services

import android.content.Context
import android.content.Intent
import com.daneart.sirpompodor.receivers.TimerNotificationActionReceiver

class NotificationUtil{
    companion object{
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Pompodoro Timer"
        private const val TIMER_ID = 0

        fun showTimeExpired(context: Context){
            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            //startIntent.action  = AppConstants
        }
    }
}