package com.group2.sinow.data.network.api.datasource

import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpRequest
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.service.SinowApiService
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface SinowDataSource {



    suspend fun doLogin(loginRequest: LoginRequest) : LoginResponse

    suspend fun registerUser(registerRequest: RegisterRequest): RegisterResponse

    suspend fun verifyEmail(otpRequest: VerifyEmailRequest): VerifyEmailResponse

    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest): ResendOtpResponse

    suspend fun getCategories(): CategoriesResponse
    suspend fun getCourses(
        search: String? = null,
        type: String? = null,
        category: Int? = null,
        level: String? = null,
        sortBy: String? = null
    ): CoursesResponse

    suspend fun getNotification(): NotificationResponse

    suspend fun getNotificationDetail(id: Int): NotificationDetailResponse

    suspend fun deleteNotification(id: Int): DeleteNotificationResponse

}

class SinowApiDataSource(private val service: SinowApiService) : SinowDataSource {
    override suspend fun doLogin(loginRequest: LoginRequest): LoginResponse {
        return service.doLogin(loginRequest)
    }


    override suspend fun registerUser(registerRequest: RegisterRequest): RegisterResponse {
        return service.registerUser(registerRequest)
    }

    override suspend fun verifyEmail(otpRequest: VerifyEmailRequest): VerifyEmailResponse {
        return service.verifyEmail(otpRequest)
    }

    override suspend fun resendOtp(resendOtpRequest: ResendOtpRequest): ResendOtpResponse {
        return service.resendOtp(resendOtpRequest)
    }


    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun getCourses(
        search: String?,
        type: String?,
        category: Int?,
        level: String?,
        sortBy: String?
    ): CoursesResponse {
        return service.getCourses(search, type, category, level, sortBy)
    }

    override suspend fun getNotification(): NotificationResponse {
        return service.getNotifications()
    }

    override suspend fun getNotificationDetail(id: Int): NotificationDetailResponse {
        return service.getNotificationDetail(id)
    }

    override suspend fun deleteNotification(id: Int): DeleteNotificationResponse {
        return service.deleteNotification(id)
    }

}