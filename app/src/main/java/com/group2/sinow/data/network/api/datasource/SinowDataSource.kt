package com.group2.sinow.data.network.api.datasource

import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordRequest
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpRequest
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordRequest
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordResponse
import com.group2.sinow.data.network.api.model.profile.ProfileResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.updateprofile.UpdateUserDataResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.data.network.api.service.SinowApiService
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SinowDataSource {



    suspend fun doLogin(loginRequest: LoginRequest) : LoginResponse

    suspend fun registerUser(registerRequest: RegisterRequest): RegisterResponse

    suspend fun verifyEmail(otpRequest: VerifyEmailRequest): VerifyEmailResponse

    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest): ResendOtpResponse

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse

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

    suspend fun getUserData(): ProfileResponse

    suspend fun updateUserData(
        name: RequestBody?,
        email: RequestBody?,
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ): UpdateUserDataResponse

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse

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

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse {
        return service.resetPassword(resetPasswordRequest)
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

    override suspend fun getUserData(): ProfileResponse {
        return service.getUserData()
    }

    override suspend fun updateUserData(
        name: RequestBody?,
        email: RequestBody?,
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ): UpdateUserDataResponse {
        return service.updateUserData(name, email, phoneNumber, country, city, image)
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse {
        return service.changePassword(changePasswordRequest)
    }

}