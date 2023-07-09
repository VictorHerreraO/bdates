package com.soyvictorherrera.bdates.core.network

import javax.inject.Inject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit

class ApiAuthenticator @Inject constructor(
    private val retrofit: Retrofit,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        return response.request
    }
}
