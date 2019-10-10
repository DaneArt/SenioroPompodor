package com.daneart.sirpompodor.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProviders
import androidx.work.WorkManager
import com.daneart.sirpompodor.R
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState
import com.daneart.sirpompodor.models.Timer
import com.daneart.sirpompodor.utils.NotificationUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.fragment_timer.*


class TimerFragment : Fragment() {

    private val TAG = TimerFragment::class.java.simpleName

    private lateinit var timerObject: Timer

    private lateinit var timer: CountDownTimer

    private lateinit var timerProgressBar: ProgressBar
    private lateinit var timerTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timerObject = Timer
        timerObject.prefereces = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        timerProgressBar = view.findViewById(R.id.pb_timer)
        timerTextView = view.findViewById(R.id.txt_timer)

        view.findViewById<FloatingActionButton>(R.id.fab_controls).setOnClickListener {

            if (timerObject.timerState == TimerState.Running) {
                timer.cancel()
                timerObject.timerState = TimerState.Paused
                updateButtons()
            } else {
                startTimer()
                timerObject.timerState = TimerState.Running
                updateButtons()
            }

        }

        view.findViewById<Button>(R.id.btn_reset_timer).setOnClickListener {
            timer.cancel()
            timerObject.timerState = TimerState.Stopped
            initTimer()
        }

        view.findViewById<AppCompatImageView>(R.id.btn_skip_timer).setOnClickListener {
            if(timerObject.timerState == TimerState.Running)timer.cancel()
            onTimerFinished()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "Timer fragment onResume()")
        initTimer()

        NotificationUtil.hideTimerNotification(context!!)
    }

    override fun onPause() {
        super.onPause()

        if (timerObject.timerState == TimerState.Running) timer.cancel()
    }

    private fun initTimer() {

        if (timerObject.timerState == TimerState.Stopped)
            timerObject.secondsRemaining = timerObject.currentTimerLength * 60L

        timerProgressBar.max = (timerObject.currentTimerLength * 60L).toInt()

        if(timerObject.secondsRemaining <= 0)
            onTimerFinished()
        else if(timerObject.timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateUI()

    }

    private fun startTimer() {

        timerObject.timerState = TimerState.Running
        updateButtons()

        timer = object : CountDownTimer(timerObject.secondsRemaining * 1000, 1000) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                timerObject.secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }

        }

        timer.start()

    }

    private fun onTimerFinished() {

        timerObject.timerState = TimerState.Stopped

        if (timerObject.processState != ProcessState.Work)
            timerObject.currentCycle++

        timerObject.updateProcessState()

        timerProgressBar.max = (timerObject.currentTimerLength * 60L).toInt()
        timerProgressBar.progress = timerProgressBar.max

        timerObject.secondsRemaining = timerObject.currentTimerLength * 60L

        updateButtons()
        updateUI()

        if (timerObject.isAutostart)
            startTimer()
    }

    private fun updateUI() {
        if (timerObject.processState == ProcessState.Work) {
            timerProgressBar.progressDrawable.setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            timerProgressBar.progressDrawable.setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        txt_cycleCount.text =
            "${(timerObject.currentCycle % timerObject.cyclesCount!!) + 1}/${timerObject.cyclesCount!!}"

        updateCountdownUI()
    }

    private fun updateButtons() {
        when (timerObject.timerState) {
            TimerState.Running -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_pause))
                btn_reset_timer.isEnabled = true
            }
            TimerState.Paused -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_start))
                btn_reset_timer.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_start))
                btn_reset_timer.isEnabled = false
            }

        }
    }

    private fun updateCountdownUI() {
        val minutestUntilFinished = timerObject.secondsRemaining / 60
        val secondsInMinuteUntilFinished =
            timerObject.secondsRemaining - minutestUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timerTextView.text =
            "$minutestUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        timerProgressBar.progress = timerObject.secondsRemaining.toInt()


    }

}
