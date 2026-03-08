package com.soyvictorherrera.bdates.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.soyvictorherrera.bdates.databinding.ActivityHomeNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

import androidx.core.view.WindowCompat

@AndroidEntryPoint
class HomeNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge with light icons for the dark background
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        
        val binding = ActivityHomeNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
