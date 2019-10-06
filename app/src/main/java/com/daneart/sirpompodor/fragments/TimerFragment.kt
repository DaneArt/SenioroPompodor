package com.daneart.sirpompodor.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProviders
import com.daneart.sirpompodor.R
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState
import com.daneart.sirpompodor.models.TimerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.fragment_timer.*


class TimerFragment : Fragment() {

    private lateinit var timerViewModel: TimerViewModel

    private lateinit var timer: CountDownTimer

    private lateinit var timerProgressBar: ProgressBar
    private lateinit var timerTextView: TextView


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

        timerProgressBar = view.findViewById(R.id.pb_timer)
        timerTextView = view.findViewById(R.id.txt_timer)

        view.findViewById<FloatingActionButton>(R.id.fab_controls).setOnClickListener {

            if (timerViewModel.timerState == TimerState.Running) {
                timer.cancel()
                timerViewModel.timerState = TimerState.Paused
                updateButtons()
            } else {
                startTimer()
                timerViewModel.timerState = TimerState.Running
                updateButtons()
            }

        }

        view.findViewById<Button>(R.id.btn_reset_timer).setOnClickListener {
            timer.cancel()
            timerViewModel.timerState = TimerState.Stopped
            initTimer()
        }

        view.findViewById<AppCompatImageView>(R.id.btn_skip_timer).setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }



        return view
    }

    override fun onResume() {
        super.onResume()

        initTimer()
    }

    fun initTimer() {
        timerViewModel.secondsRemaining = timerViewModel.currentTimerLength!! * 60L

        timerProgressBar.max = (timerViewModel.currentTimerLength!! * 60L).toInt()

        updateButtons()
        updateUI()

    }

    fun startTimer() {

        timerViewModel.timerState = TimerState.Running
        updateButtons()

        timer = object : CountDownTimer(timerViewModel.secondsRemaining * 1000, 1000) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                timerViewModel.secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }

        }

        timer.start()

    }

    private fun onTimerFinished() {

        timerViewModel.timerState = TimerState.Stopped

        if(timerViewModel.processState != ProcessState.Work)
            timerViewModel.currentCycle++

        timerViewModel.updateProcessState()

        timerProgressBar.max = (timerViewModel.currentTimerLength!! * 60L).toInt()
        timerProgressBar.progress = timerProgressBar.max

        timerViewModel.secondsRemaining = timerViewModel.currentTimerLength!! * 60L

        updateButtons()
        updateUI()

        if(timerViewModel.isAutostart!!)
            startTimer()
    }

    private fun updateUI() {
        if (timerViewModel.processState == ProcessState.Work) {
            timerProgressBar.progressDrawable.setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            timerProgressBar.progressDrawable.setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        txt_cycleCount.text =
            "${(timerViewModel.currentCycle % timerViewModel.cyclesCount!!) + 1}/${timerViewModel.cyclesCount!!}"

        updateCountdownUI()
    }

    private fun updateButtons() {
        when (timerViewModel.timerState) {
            TimerState.Running -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_pause))
                btn_skip_timer.isEnabled = true
                btn_reset_timer.isEnabled = true
            }
            TimerState.Paused -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_start))
                btn_skip_timer.isEnabled = true
                btn_reset_timer.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_controls.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_start))
                btn_skip_timer.isEnabled = false
                btn_reset_timer.isEnabled = false
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        try{
            timer.cancel()
        }catch (t:Throwable){}
    }

    private fun updateCountdownUI() {
        val minutestUntilFinished = timerViewModel.secondsRemaining / 60
        val secondsInMinuteUntilFinished =
            timerViewModel.secondsRemaining - minutestUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timerTextView.text =
            "$minutestUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        timerProgressBar.progress = timerViewModel.secondsRemaining.toInt()


    }

}
