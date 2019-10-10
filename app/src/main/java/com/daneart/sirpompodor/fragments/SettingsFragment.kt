package com.daneart.sirpompodor.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation

import com.daneart.sirpompodor.R
import com.daneart.sirpompodor.models.Timer

class SettingsFragment : Fragment() {

    private val TAG = SettingsFragment::class.java.simpleName

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.findViewById<AppCompatImageView>(R.id.btn_pop_settings).setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(activity)

        val workTimeTxt = view.findViewById<TextView>(R.id.txt_workTime)
        val srestTimeTxt = view.findViewById<TextView>(R.id.txt_srestTime)
        val lrestTimeTxt = view.findViewById<TextView>(R.id.txt_lrestTime)
        val cyclesCountTxt = view.findViewById<TextView>(R.id.txt_cyclesCount)

        workTimeTxt.text =
            getString(
                R.string.work_time, "${
                preferences.getLong(Timer.WORK_TIME_ID, 25)
                }:00"
            )
        srestTimeTxt.text =
            getString(
                R.string.Srest_time, "${
                preferences.getLong(Timer.SREST_TIME_ID, 5)
                }:00"
            )
        lrestTimeTxt.text =
            getString(
                R.string.Lrest_time, "${
                preferences.getLong(Timer.LREST_TIME_ID, 15)
                }:00"
            )
        cyclesCountTxt.text =
            getString(
                R.string.cycles_count,
                preferences.getInt(Timer.CYCLES_COUNT_ID, 4)
            )

        val workSeekBar = view.findViewById<SeekBar>(R.id.seek_workTime)
        val srestSeekBar = view.findViewById<SeekBar>(R.id.seek_srestTime)
        val lrestSeekBar = view.findViewById<SeekBar>(R.id.seek_lrestTime)
        val cyclesSeekBar = view.findViewById<SeekBar>(R.id.seek_cyclesCount)

        workSeekBar.progress = preferences.getLong(Timer.WORK_TIME_ID, 25).toInt()
        srestSeekBar.progress = preferences.getLong(Timer.SREST_TIME_ID, 5).toInt()
        lrestSeekBar.progress = preferences.getLong(Timer.LREST_TIME_ID, 15).toInt()
        cyclesSeekBar.progress = preferences.getInt(Timer.CYCLES_COUNT_ID, 4)

        val autostartCheckBox:CheckBox = view.findViewById(R.id.chkbx_autostart)
        autostartCheckBox.isChecked = preferences.getBoolean(Timer.TIMER_AUTOSTART_ID, false)

        autostartCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isPressed){
                val editor = preferences.edit()
                editor.putBoolean(Timer.TIMER_AUTOSTART_ID,compoundButton.isChecked)
                Log.e(TAG,"AutoStart changed: ${Timer.isAutostart}")
                editor.apply()
            }
        }

        workSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                    workTimeTxt.text =
                        getString(R.string.work_time, "$progress:00")
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor = preferences.edit()
                    editor.putLong(
                        Timer.WORK_TIME_ID,
                        seekBar?.progress!!.toLong()
                    )
                    editor.apply()
                }

            })
        srestSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                    srestTimeTxt.text =
                        getString(R.string.Srest_time, "$progress:00")
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
                    editor.putLong(
                        Timer.SREST_TIME_ID,
                        seekBar?.progress!!.toLong()
                    )
                    editor.apply()
                }

            })
        lrestSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                    lrestTimeTxt.text =
                        getString(R.string.Lrest_time, "$progress:00")
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
                    editor.putLong(
                        Timer.LREST_TIME_ID,
                        seekBar?.progress!!.toLong()
                    )
                    editor.apply()
                }

            })
        cyclesSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                    cyclesCountTxt.text =
                        getString(R.string.cycles_count, progress)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
                    editor.putInt(
                        Timer.CYCLES_COUNT_ID,
                        seekBar?.progress!!
                    )
                    editor.apply()
                }

            })


        return view
    }


}
