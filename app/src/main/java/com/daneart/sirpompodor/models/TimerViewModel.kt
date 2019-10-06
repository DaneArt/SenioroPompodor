package com.daneart.sirpompodor.models

import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState

class TimerViewModel : ViewModel() {
    fun updateProcessState() {
        processState = if (processState == ProcessState.Work) {
            if (((currentCycle+1) % cyclesCount!!) == 0) {
                ProcessState.LongRest
            } else
                ProcessState.ShortRest
        } else {
            ProcessState.Work
        }
    }
    companion object{
        const val CYCLES_COUNT_ID = "com.daneart.pompodoro.cycles_count"
        const val WORK_TIME_ID = "com.daneart.pompodoro.work_time"
        const val SREST_TIME_ID = "com.daneart.pompodoro.srest_time"
        const val LREST_TIME_ID = "com.daneart.pompodoro.lrest_time"
        const val TIMER_AUTOSTART_ID = "com.daneart.pompodoro.timer_autostart"
    }


    var prefereces: SharedPreferences? = null

    var cyclesCount: Int? = 4
        get() {
            return prefereces?.getInt(CYCLES_COUNT_ID, 4)
        }
    var workTime: Long? = 25
        get() {
            return prefereces?.getLong(WORK_TIME_ID, 25)
        }
    var srestTime: Long? = 5
        get() {
            return prefereces?.getLong(SREST_TIME_ID, 5)
        }
    var lrestTime: Long? = 15
        get() {
            return prefereces?.getLong(LREST_TIME_ID, 15)
        }
    var isAutostart: Boolean? = true
        get() {
            return prefereces?.getBoolean(TIMER_AUTOSTART_ID, true)
        }
    var currentCycle: Int = 0
    var timerState: TimerState = TimerState.Stopped
    var processState: ProcessState = ProcessState.Work
    var secondsRemaining: Long = 25 * 60
    var currentTimerLength: Long? = null
        get() {
            return when (processState) {
                ProcessState.Work -> workTime
                ProcessState.ShortRest -> srestTime
                else -> lrestTime
            }
        }

}