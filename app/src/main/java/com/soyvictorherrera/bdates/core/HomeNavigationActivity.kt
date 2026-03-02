package com.soyvictorherrera.bdates.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soyvictorherrera.bdates.databinding.ActivityHomeNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

import androidx.core.view.WindowCompat

@AndroidEntryPoint
class HomeNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        val controller = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = false // Always light icons over dark purple backdrop
        controller.isAppearanceLightNavigationBars = false // Always light icons over dark purple backdrop

        val binding = ActivityHomeNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
