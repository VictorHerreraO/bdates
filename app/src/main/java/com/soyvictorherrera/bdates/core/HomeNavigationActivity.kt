package com.soyvictorherrera.bdates.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soyvictorherrera.bdates.databinding.ActivityHomeNavigationBinding
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeNavigationActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManagerContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager.setupDayEventsReminder()
    }

}
