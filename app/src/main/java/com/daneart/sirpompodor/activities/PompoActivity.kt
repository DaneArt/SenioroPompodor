package com.daneart.sirpompodor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.daneart.sirpompodor.utils.NotificationUtil
import com.daneart.sirpompodor.R
import com.daneart.sirpompodor.models.Timer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.daneart.sirpompodor.Workers.TimerWorker
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState


class PompoActivity : AppCompatActivity() {

    private val TAG = PompoActivity::class.java.simpleName

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pompo)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    fun onOpenSettingsFragment(view: View) {
        navController.navigate(R.id.action_timerFragment_to_settingsFragment)
    }

    fun onOpenAboutFragment(view: View) {
        navController.navigate(R.id.action_timerFragment_to_aboutFragment)
    }

    override fun onPause() {
        super.onPause()

        if (Timer.timerState == TimerState.Running) {
            NotificationUtil.showTimerRunning(
                context = this, processType = when (Timer.processState) {
                    ProcessState.Work -> "Work!"
                    ProcessState.ShortRest -> "Rest some time"
                    ProcessState.LongRest -> "Take a coffee break"
                }
            )
            val myWorkRequest = OneTimeWorkRequest.Builder(TimerWorker::class.java).build()
            WorkManager.getInstance().enqueue(myWorkRequest)

        } else if (Timer.timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(
                context = this, processType = when (Timer.processState) {
                    ProcessState.Work -> "Work!"
                    ProcessState.ShortRest -> "Rest some time"
                    ProcessState.LongRest -> "Take a coffee break"
                }
            )
        }

    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()
        WorkManager.getInstance().cancelAllWork()
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onDestroy() {
        Log.e(TAG, "Main Activity destroyed")
        WorkManager.getInstance().cancelAllWork()
        NotificationUtil.hideTimerNotification(this)
        super.onDestroy()
    }
}
