package com.group2.sinow.data.repository

import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpRequest
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun doLogin(email: String, password: String): Flow<ResultWrapper<LoginResponse>>

    fun registerUser(registerRequest: RegisterRequest): Flow<ResultWrapper<RegisterResponse>>

    fun verifyEmail(otpRequest: VerifyEmailRequest): Flow<ResultWrapper<VerifyEmailResponse>>

    fun resendOtp(email: String): Flow<ResultWrapper<ResendOtpResponse>>
}

class AuthRepositoryImpl(private val dataSource: SinowDataSource
):AuthRepository{


    override fun doLogin(email: String, password: String): Flow<ResultWrapper<LoginResponse>> {
        return proceedFlow {
            dataSource.doLogin(LoginRequest(email, password))
        }
    }


    override fun registerUser(registerRequest: RegisterRequest): Flow<ResultWrapper<RegisterResponse>> {
        return proceedFlow {
            dataSource.registerUser(registerRequest)
        }
    }

    override fun verifyEmail(otpRequest: VerifyEmailRequest): Flow<ResultWrapper<VerifyEmailResponse>> {
        return proceedFlow {
            dataSource.verifyEmail(otpRequest)
        }
    }

    override fun resendOtp(email: String): Flow<ResultWrapper<ResendOtpResponse>> {
        return proceedFlow {
            dataSource.resendOtp(ResendOtpRequest(email))
        }
    }

}