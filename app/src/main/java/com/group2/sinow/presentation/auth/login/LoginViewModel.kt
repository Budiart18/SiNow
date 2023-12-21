package com.group2.sinow.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.local.UserPreferenceDataSource
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    private val userPreferenceDataSource: UserPreferenceDataSource
) : ViewModel() {

    private val _userToken = MutableLiveData<String?>()
    val userToken: LiveData<String?>
        get() = _userToken

    private val _loginResult = MutableLiveData<ResultWrapper<LoginResponse>>()
    val loginResult: LiveData<ResultWrapper<LoginResponse>>
        get() = _loginResult

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.doLogin(email, password).collect{
                _loginResult.postValue(it)
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            userPreferenceDataSource.saveUserToken(token)
        }
    }

    init {
        viewModelScope.launch {
            userPreferenceDataSource.getUserTokenFlow()
        }
    }

}