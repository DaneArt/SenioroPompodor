package com.daneart.sirpompodor.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation

import com.daneart.sirpompodor.R
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.findViewById<AppCompatImageView>(R.id.btn_pop_about).setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        btn_twitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/DaneArt1"))
            startActivity(intent)
        }
        btn_github.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/DaneArt"))
            startActivity(intent)
        }

        return view
    }


}
