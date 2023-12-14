package com.group2.sinow.data.network.api.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.BuildConfig
import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.model.profile.ProfileResponse
import com.group2.sinow.data.network.api.model.updateprofile.UpdateUserDataResponse
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
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

    @DELETE("user/notifications/{id}")
    suspend fun deleteNotification(@Path("id") id : Int) : DeleteNotificationResponse

    @GET("user")
    suspend fun getUserData() : ProfileResponse

    @Multipart
    @PATCH("user/update")
    suspend fun updateUserData(
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("country") country: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part image: MultipartBody.Part?
    ) : UpdateUserDataResponse

    companion object {
        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor): SinowApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chucker)
                .addInterceptor(createAuthorizationInterceptor("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjEsIm5hbWUiOiJSYWdpbCIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNzAyNDgxMzk0LCJleHAiOjE3MDI1Njc3OTQsImlzcyI6IlNpTm93X1NlY3VyaXR5In0.QfnuJoB62-ut_9sdnNFFoN8X_Vq8liRH5I5td7h7JvU"))
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

        private fun createAuthorizationInterceptor(token: String?): Interceptor {
            return Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }
    }

}