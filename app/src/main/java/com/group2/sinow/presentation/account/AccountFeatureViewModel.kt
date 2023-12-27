package com.group2.sinow.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.group2.sinow.data.local.UserPreferenceDataSource
import kotlinx.coroutines.Dispatchers

class AccountFeatureViewModel(
    private val userPreferenceDataSource: UserPreferenceDataSource
): ViewModel() {

    val userDarkMode = userPreferenceDataSource.getUserDarkModePrefFlow()
}