package com.group2.sinow.presentation.auth.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.data.local.UserPreferenceDataSource
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.launch

class OTPViewModel(private val repository: AuthRepository, private val userPreferenceDataSource: UserPreferenceDataSource): ViewModel() {

    private val _verificationState = MutableLiveData<ResultWrapper<VerifyEmailResponse>>()
    val verificationState: LiveData<ResultWrapper<VerifyEmailResponse>>
        get() = _verificationState

    private val _resendOtpStatus = MutableLiveData<ResultWrapper<ResendOtpResponse>>()
    val resendOtpStatus: LiveData<ResultWrapper<ResendOtpResponse>> = _resendOtpStatus

    fun verifyEmail(verifyEmailRequest: VerifyEmailRequest) {
        viewModelScope.launch {
            repository.verifyEmail(verifyEmailRequest).collect { result ->
                _verificationState.postValue(result)
            }
        }
    }

    fun resendOtp(email: String){
        viewModelScope.launch {
            repository.resendOtp(email).collect { result ->
                _resendOtpStatus.postValue(result)
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            userPreferenceDataSource.saveUserToken(token)
        }
    }
}