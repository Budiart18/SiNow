package com.group2.sinow.data.repository

import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordRequest
import com.group2.sinow.data.network.api.model.profile.toProfileData
import com.group2.sinow.data.network.api.model.transactionhistory.toItemTransaction
import com.group2.sinow.data.network.api.model.transactionhistory.toTransactionList
import com.group2.sinow.data.network.api.model.userclass.toUserListCourseData
import com.group2.sinow.model.paymenthistory.TransactionUser
import com.group2.sinow.model.profile.ProfileData
import com.group2.sinow.model.userclass.UserCourseData
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {

    fun getUserData(): Flow<ResultWrapper<ProfileData>>

    suspend fun updateUserData(
        name: RequestBody?,
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ): Flow<ResultWrapper<Boolean>>

    fun changePassword(
        oldPassword: String?,
        newPassword: String?,
        confirmNewPassword: String?
    ): Flow<ResultWrapper<Boolean>>

    fun getUserCourses(
        search: String? = null,
        progress: String? = null
    ): Flow<ResultWrapper<List<UserCourseData>>>

    fun getUserTransactionHistory(): Flow<ResultWrapper<List<TransactionUser>>>

    fun getUserDetailTransaction(id: String): Flow<ResultWrapper<TransactionUser>>

    fun deleteTransaction(transactionId: String): Flow<ResultWrapper<Boolean>>
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
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val token = dataSource.updateUserData(name, phoneNumber, country, city, image).data?.token
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

    override fun getUserTransactionHistory(): Flow<ResultWrapper<List<TransactionUser>>> {
        return proceedFlow {
            dataSource.getUserTransactionHistory().data?.transactions?.toTransactionList()
                ?: emptyList()
        }
    }

    override fun getUserDetailTransaction(id: String): Flow<ResultWrapper<TransactionUser>> {
        return proceedFlow {
            dataSource.getUserDetailTransaction(id).data.transaction.toItemTransaction()
        }
    }

    override fun deleteTransaction(transactionId: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            dataSource.deleteTransaction(transactionId).status == "Success"
        }
    }

    override fun getUserCourses(
        search: String?,
        progress: String?
    ): Flow<ResultWrapper<List<UserCourseData>>> {
        return proceedFlow {
            dataSource.getUserCourse(search, progress).data?.toUserListCourseData() ?: emptyList()
        }
    }
}
