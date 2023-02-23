package com.soyvictorherrera.bdates

import android.app.Application
import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCaseContract
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class BdatesApp : Application() {

    @Inject
    lateinit var createLocalCircle: CreateLocalCircleUseCaseContract

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        MainScope().launch {
            createLocalCircle.execute()
        }
    }
}
