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
        processState = if(processState == ProcessState.Work){
            if((currentCycle % cyclesCount)+1 == cyclesCount){
                ProcessState.LongRest
            }else
                ProcessState.ShortRest
        }else{
            ProcessState.Work
        }
    }

    private val CYCLES_COUNT_ID = "com.daneart.pompodoro.cycles_count"
    private val WORK_TIME_ID = "com.daneart.pompodoro.work_time"
    private val SREST_TIME_ID = "com.daneart.pompodoro.srest_time"
    private val LREST_TIME_ID = "com.daneart.pompodoro.lrest_time"
    private val TIMER_AUTOSTART_ID = "com.daneart.pompodoro.timer_autostart"

    var prefereces: SharedPreferences? = null

    var cyclesCount = prefereces!!.getInt(CYCLES_COUNT_ID, 4)
    var workTime = prefereces!!.getLong(WORK_TIME_ID, 25)
    var srestTime = prefereces!!.getLong(SREST_TIME_ID, 5)
    var lrestTime = prefereces!!.getLong(LREST_TIME_ID, 15)
    var isAutostart = prefereces!!.getBoolean(TIMER_AUTOSTART_ID, true)
    var currentCycle: Int = 0
    var timerState: TimerState = TimerState.Stopped
    var processState: ProcessState = ProcessState.Work
    var secondsRemaining: Long = 0
    var currentTimerLength: Long? = null
        get() {
        return when (processState) {
            ProcessState.Work -> workTime
            ProcessState.ShortRest -> srestTime
            else -> lrestTime
        }
    }

}