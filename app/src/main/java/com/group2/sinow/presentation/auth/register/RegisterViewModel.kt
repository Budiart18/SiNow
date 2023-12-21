package com.group2.sinow.presentation.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository): ViewModel() {

    private val _registerState = MutableLiveData<ResultWrapper<RegisterResponse>>()
    val registerState: LiveData<ResultWrapper<RegisterResponse>> = _registerState

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            repository.registerUser(registerRequest).collect { result ->
                _registerState.value = result
            }
        }
    }
}