package com.soyvictorherrera.bdates.core.network

import com.soyvictorherrera.bdates.BuildConfig

object Endpoints {

    val BASE_URL: String
        get() = if (BuildConfig.DEBUG) {
            "https://us-west2-bdates-sandbox.cloudfunctions.net/app/"
        } else {
            "example.com"
        }

}