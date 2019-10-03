package com.daneart.sirpompodor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.daneart.sirpompodor.R

class PompoActivity : AppCompatActivity() {

    private val TAG = PompoActivity::class.java.simpleName

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pompo)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
    }

    fun onOpenSettingsFragment(view: View){
        navController.navigate(R.id.action_timerFragment_to_settingsFragment)
    }

    fun onOpenAboutFragment(view: View){
        navController.navigate(R.id.action_timerFragment_to_aboutFragment)
    }

}
