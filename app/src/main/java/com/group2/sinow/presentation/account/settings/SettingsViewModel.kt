package com.group2.sinow.presentation.account.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.local.UserPreferenceDataSource
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferenceDataSource: UserPreferenceDataSource
) : ViewModel() {

    fun setUserDarkModePref(isUsingDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferenceDataSource.setUserDarkModePref(isUsingDarkMode)
        }
    }
}
