package com.example.teaapp.data.util

import com.example.teaapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Intercepts every HTTP request and adds the required auth header.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Auth-Token", BuildConfig.API_TOKEN)
            .build()

        return chain.proceed(request)
    }
}
