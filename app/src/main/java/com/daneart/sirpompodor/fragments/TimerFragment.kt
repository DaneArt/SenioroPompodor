package com.daneart.sirpompodor.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.daneart.sirpompodor.R
import com.daneart.sirpompodor.helpers.TimerState
import com.daneart.sirpompodor.models.TimerViewModel
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlin.concurrent.timer


class TimerFragment : Fragment() {

    private lateinit var timerViewModel: TimerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timerViewModel = ViewModelProviders.of(this).get(TimerViewModel::class.java)
        timerViewModel.prefereces = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_timer, container, false)
        return view
    }

    fun startTimer() {

        timerViewModel.timerState = TimerState.Started

       object : CountDownTimer(timerViewModel.secondsRemaining, 1000) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                timerViewModel.secondsRemaining = millisUntilFinished
                updateCountdownUI()
            }

        }.start()

    }

    private fun onTimerFinished() {

        timerViewModel.timerState = TimerState.Stopped
        timerViewModel.updateProcessState()

        timerViewModel.currentCycle++
        pb_timer.progress = pb_timer.max

        timerViewModel.secondsRemaining = timerViewModel.currentTimerLength!!

        updateButtons()

    }

    private fun updateButtons() {
        when (timerViewModel.timerState) {
            TimerState.Started -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_pause))
            }
        }
    }

    private fun updateCountdownUI() {
        val minutestUntilFinished = timerViewModel.secondsRemaining / 60
        val secondsInMinuteUntilFinished =
            timerViewModel.secondsRemaining - minutestUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        txt_timer.text =
            "$minutestUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        pb_timer.progress = timerViewModel.secondsRemaining.toInt()
    }

}
