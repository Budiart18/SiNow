package com.group2.sinow.data.network.api.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.BuildConfig
import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.presentation.auth.login.UserPreferenceDataSource
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpRequest
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordRequest
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface SinowApiService {


    // Login
    @POST("auth/login")
    suspend fun doLogin(@Body loginRequest: LoginRequest) : LoginResponse

    // Token
    @GET("auth/check-token")
    suspend fun checkToken(@Header("Authorization") token: String) : LoginResponse


    // Register
    @POST("auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    // Verify Email / OTP
    @POST("auth/verify-email")
    suspend fun verifyEmail(@Body otpRequest: VerifyEmailRequest): VerifyEmailResponse

    // Resend OTP
    @POST("auth/resend-otp")
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): ResendOtpResponse

    // Reset Password
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse



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

    companion object {

        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor, userPreferenceDataSource: UserPreferenceDataSource): SinowApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chucker)
                .addInterceptor(AuthInterceptor(userPreferenceDataSource))
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