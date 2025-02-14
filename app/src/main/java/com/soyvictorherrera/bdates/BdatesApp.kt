package com.soyvictorherrera.bdates

import android.app.Application
import com.soyvictorherrera.bdates.core.delegate.ApplicationCreatedCallbackDelegateContract
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BdatesApp : Application() {

    @Inject
    lateinit var applicationCreatedCallback: ApplicationCreatedCallbackDelegateContract

    override fun onCreate() {
        super.onCreate()
        applicationCreatedCallback.onApplicationCreated()
    }
}
