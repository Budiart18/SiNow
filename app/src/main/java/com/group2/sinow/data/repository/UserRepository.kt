package com.group2.sinow.data.repository

import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordRequest
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordResponse
import com.group2.sinow.data.network.api.model.profile.toProfileData
import com.group2.sinow.model.profile.ProfileData
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {

    fun getUserData(): Flow<ResultWrapper<ProfileData>>

    suspend fun updateUserData(
        name: RequestBody?,
        email: RequestBody?,
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ) : Flow<ResultWrapper<Boolean>>

    fun changePassword(
        oldPassword: String?,
        newPassword: String?,
        confirmNewPassword: String?
    ) : Flow<ResultWrapper<Boolean>>
}

class UserRepositoryImpl(
    private val dataSource: SinowDataSource
) : UserRepository {

    override fun getUserData(): Flow<ResultWrapper<ProfileData>> {
        return proceedFlow {
            dataSource.getUserData().data.toProfileData()
        }
    }

    override suspend fun updateUserData(
        name: RequestBody?,
        email: RequestBody?,
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val token = dataSource.updateUserData(name, email, phoneNumber, country, city, image).data?.token
            token != null
        }
    }

    override fun changePassword(
        oldPassword: String?,
        newPassword: String?,
        confirmNewPassword: String?
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val request = ChangePasswordRequest(oldPassword, newPassword, confirmNewPassword)
            dataSource.changePassword(request).message != null
        }
    }

}