package com.group2.sinow.presentation.change_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePasswordUserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _changePasswordResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changePasswordResult: LiveData<ResultWrapper<Boolean>>
        get() = _changePasswordResult

    fun changePassword(
        oldPassword: String?,
        newPassword: String?,
        confirmNewPassword: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changePassword(oldPassword, newPassword, confirmNewPassword).collect{
                _changePasswordResult.postValue(it)
            }
        }
    }
}