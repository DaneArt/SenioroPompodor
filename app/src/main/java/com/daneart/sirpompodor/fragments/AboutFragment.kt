package com.daneart.sirpompodor.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation

import com.daneart.sirpompodor.R



class AboutFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() =
            AboutFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.findViewById<AppCompatImageView>(R.id.btn_pop_about).setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
        return view
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


}
