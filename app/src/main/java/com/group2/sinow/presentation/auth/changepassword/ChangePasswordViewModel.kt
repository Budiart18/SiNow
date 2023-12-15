package com.group2.sinow.presentation.auth.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordResponse
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: AuthRepository): ViewModel() {

    private val _resetStatus = MutableLiveData<ResultWrapper<ResetPasswordResponse>>()
    val resetStatus: LiveData<ResultWrapper<ResetPasswordResponse>>
        get() = _resetStatus


    fun resetPassword(email: String){
        viewModelScope.launch {
            repository.resetPassword(email).collect { result ->
                _resetStatus.postValue(result)
            }
        }
    }

}