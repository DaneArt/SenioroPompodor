package com.daneart.sirpompodor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.daneart.sirpompodor.Workers.TimerWorker
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState
import com.daneart.sirpompodor.models.Timer
import com.daneart.sirpompodor.utils.NotificationUtil

class TimerAutoStartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TimerAutoStartReceiver::class.java.canonicalName, "Broadcast received")
        if (Timer.timerState == TimerState.Running) {
            NotificationUtil.showTimerRunning(
                context = context, processType = when (Timer.processState) {
                    ProcessState.Work -> "Work!"
                    ProcessState.ShortRest -> "Rest some time"
                    ProcessState.LongRest -> "Take a coffee break"
                }
            )
            val myWorkRequest = OneTimeWorkRequest.Builder(TimerWorker::class.java).build()
            WorkManager.getInstance().enqueue(myWorkRequest)
        } else if (Timer.timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(
                context = context, processType = when (Timer.processState) {
                    ProcessState.Work -> "Work!"
                    ProcessState.ShortRest -> "Rest some time"
                    ProcessState.LongRest -> "Take a coffee break"
                }
            )
        }
    }

}