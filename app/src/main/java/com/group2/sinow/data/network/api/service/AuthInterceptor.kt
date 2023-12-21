package com.group2.sinow.data.network.api.service

import com.group2.sinow.data.local.UserPreferenceDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userPreferenceDataSource: UserPreferenceDataSource) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        runBlocking {
            val token = userPreferenceDataSource.getUserTokenFlow().firstOrNull()
            token.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}