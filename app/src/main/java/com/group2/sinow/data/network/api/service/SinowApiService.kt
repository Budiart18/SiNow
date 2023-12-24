package com.group2.sinow.data.network.api.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.BuildConfig
import com.group2.sinow.data.local.UserPreferenceDataSource
import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordRequest
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.detailcourse.DataResponse
import com.group2.sinow.data.network.api.model.followcourse.FollowCourseResponse
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.model.profile.ProfileResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpRequest
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordRequest
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordResponse
import com.group2.sinow.data.network.api.model.transactionhistory.DeleteTransactionResponse
import com.group2.sinow.data.network.api.model.transaction.TransactionRequest
import com.group2.sinow.data.network.api.model.transaction.TransactionResponse
import com.group2.sinow.data.network.api.model.transactionhistory.TransactionDetailResponse
import com.group2.sinow.data.network.api.model.transactionhistory.TransactionsHistoryResponse
import com.group2.sinow.data.network.api.model.updateprofile.UpdateUserDataResponse
import com.group2.sinow.data.network.api.model.userclass.ClassesResponse
import com.group2.sinow.data.network.api.model.usermodule.UserModuleDataResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface SinowApiService {


    // Login
    @POST("auth/login")
    suspend fun doLogin(@Body loginRequest: LoginRequest): LoginResponse

    // Token
    @GET("auth/check-token")
    suspend fun checkToken(@Header("Authorization") token: String): LoginResponse


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
    suspend fun getCoursesFilter(
        @Query("search") search: String? = null,
        @Query("type") type: String? = null,
        @Query("category") category: List<Int>? = null,
        @Query("level") level: List<String>? = null,
        @Query("sortBy") sortBy: String? = null
    ): CoursesResponse

    @GET("courses")
    suspend fun getCourses(
        @Query("search") search: String? = null,
        @Query("type") type: String? = null,
        @Query("category") category: Int? = null,
        @Query("level") level: String? = null,
        @Query("sortBy") sortBy: String? = null
    ): CoursesResponse

    @GET("user/notifications")
    suspend fun getNotifications(): NotificationResponse

    @GET("user/notifications/{id}")
    suspend fun getNotificationDetail(@Path("id") id: Int): NotificationDetailResponse

    @DELETE("user/notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int): DeleteNotificationResponse

    @GET("user")
    suspend fun getUserData(): ProfileResponse

    @Multipart
    @PATCH("user/update")
    suspend fun updateUserData(
        @Part("name") name: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("country") country: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part image: MultipartBody.Part?
    ): UpdateUserDataResponse

    @PATCH("user/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse

    @GET("user/my-courses")
    suspend fun getUserCourses(
        @Query("search") search: String? = null,
        @Query("progress") progress: String? = null
    ): ClassesResponse

    @GET("user/transaction")
    suspend fun getUserTransactionHistory(): TransactionsHistoryResponse

    @GET("user/transaction/{transactionId}")
    suspend fun getUserDetailTransaction(@Path("transactionId") id: String): TransactionDetailResponse


    @GET("user/my-courses/{courseId}")
    suspend fun getCourseDetail(@Path("courseId") id: Int): DataResponse

    @GET("user/my-courses/{courseId}/modules/{userModuleId}")
    suspend fun getUserModuleData(
        @Path("courseId") courseId: Int?,
        @Path("userModuleId") userModuleId: Int?
    ): UserModuleDataResponse

    @POST("user/my-courses/{courseId}/follow-course")
    suspend fun followCourse(@Path("courseId") courseId: Int?): FollowCourseResponse

    @POST("transactions")
    suspend fun buyPremiumCourse(@Body transactionRequest: TransactionRequest): TransactionResponse

    @DELETE("user/transaction/{transactionId}")
    suspend fun deleteTransaction(@Path("transactionId") transactionId: String): DeleteTransactionResponse

    companion object {

        @JvmStatic
        operator fun invoke(
            chucker: ChuckerInterceptor,
            userPreferenceDataSource: UserPreferenceDataSource
        ): SinowApiService {
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