package com.daneart.sirpompodor.models

import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState

object Timer {

    const val CYCLES_COUNT_ID = "com.daneart.pompodoro.cycles_count"
    const val WORK_TIME_ID = "com.daneart.pompodoro.work_time"
    const val SREST_TIME_ID = "com.daneart.pompodoro.srest_time"
    const val LREST_TIME_ID = "com.daneart.pompodoro.lrest_time"
    const val TIMER_AUTOSTART_ID = "com.daneart.pompodoro.timer_autostart"

    lateinit var prefereces: SharedPreferences

    var timerState: TimerState = TimerState.Stopped
    var processState: ProcessState = ProcessState.Work

    val cyclesCount: Int
        get() {
            return prefereces.getInt(CYCLES_COUNT_ID, 4)
        }
    val workTime: Long
        get() {
            return prefereces.getLong(WORK_TIME_ID, 25)
        }
    val srestTime: Long
        get() = prefereces.getLong(SREST_TIME_ID, 5)

    val lrestTime: Long
        get() {
            return prefereces.getLong(LREST_TIME_ID, 15)
        }
    val isAutostart: Boolean
        get() {
            return prefereces.getBoolean(TIMER_AUTOSTART_ID, true)
        }
    var currentCycle: Int = 0

    var secondsRemaining: Long = 25 * 60

    val currentTimerLength: Long
        get() {
            return when (processState) {
                ProcessState.Work -> workTime
                ProcessState.ShortRest -> srestTime
                else -> lrestTime
            }
        }

    fun updateProcessState() {
        processState = if (processState == ProcessState.Work) {
            if (((currentCycle + 1) % cyclesCount) == 0) {
                ProcessState.LongRest
            } else
                ProcessState.ShortRest
        } else {
            ProcessState.Work
        }
    }

}