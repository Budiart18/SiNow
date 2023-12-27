package com.group2.sinow.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.dummy.IntroPageDataSource
import com.group2.sinow.data.local.UserPreferenceDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userPreferenceDataSource: UserPreferenceDataSource,
    private val introPageDataSource: IntroPageDataSource
) : ViewModel() {

    private val _isFirstTime = MutableLiveData<Boolean>()
    val isFirstTime: LiveData<Boolean> get() = _isFirstTime

    val userDarkMode = userPreferenceDataSource.getUserDarkModePrefFlow()

    fun getIntroPageData() = introPageDataSource.getIntroPageData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            userPreferenceDataSource.getShouldShowIntroPage().collect{
                _isFirstTime.postValue(it)
            }
        }
    }

    fun setShouldShowIntroPage(isFirstTime: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            userPreferenceDataSource.setShouldShowIntroPage(isFirstTime)
        }
    }

}