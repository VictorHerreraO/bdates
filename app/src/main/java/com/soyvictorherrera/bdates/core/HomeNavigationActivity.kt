package com.soyvictorherrera.bdates.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soyvictorherrera.bdates.databinding.ActivityHomeNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeNavigationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
