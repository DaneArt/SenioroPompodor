package com.daneart.sirpompodor.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation

import com.daneart.sirpompodor.R

class SettingsFragment : Fragment() {


    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.findViewById<AppCompatImageView>(R.id.btn_pop_settings).setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
        return view
    }






}
