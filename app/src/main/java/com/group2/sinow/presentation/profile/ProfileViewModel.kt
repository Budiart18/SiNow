package com.group2.sinow.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.network.api.model.updateprofile.UpdateUserDataResponse
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.profile.ProfileData
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userData = MutableLiveData<ResultWrapper<ProfileData>>()
    val userData : LiveData<ResultWrapper<ProfileData>>
        get() = _userData

    private val _changeProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changeProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _changeProfileResult

    private val _isEditModeEnabled = MutableLiveData<Boolean>()
    val isEditModeEnabled: LiveData<Boolean>
        get() = _isEditModeEnabled

    init {
        _isEditModeEnabled.value = false
    }

    fun getUserData() {
        viewModelScope.launch {
            repository.getUserData().collect{
                _userData.postValue(it)
            }
        }
    }

    fun updateUserData(
        name: RequestBody?,
        email: RequestBody?,
        phoneNumber: RequestBody?,
        country: RequestBody?,
        city: RequestBody?,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            repository.updateUserData(name, email, phoneNumber, country, city, image).collect{
                _changeProfileResult.postValue(it)
            }
        }
    }

    fun toggleEditMode() {
        _isEditModeEnabled.value = _isEditModeEnabled.value?.not()
    }

}