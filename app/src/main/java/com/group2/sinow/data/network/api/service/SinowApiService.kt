package com.group2.sinow.data.network.api.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.BuildConfig
import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface SinowApiService {

    @GET("category")
    suspend fun getCategories(): CategoriesResponse

    @GET("courses")
    suspend fun getCourses(
        @Query("search") search: String? = null,
        @Query("type") type: String? = null,
        @Query("category") category: Int? = null,
        @Query("level") level: String? = null,
        @Query("sortBy") sortBy: String? = null
    ): CoursesResponse

    @GET("user/notifications")
    suspend fun getNotifications() : NotificationResponse

    @GET("user/notifications/{id}")
    suspend fun getNotificationDetail(@Path("id") id : Int) : NotificationDetailResponse

    companion object {
        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor): SinowApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chucker)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(SinowApiService::class.java)
        }
    }

}