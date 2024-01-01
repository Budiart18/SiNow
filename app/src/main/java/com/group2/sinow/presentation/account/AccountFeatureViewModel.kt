package com.group2.sinow.presentation.account

import androidx.lifecycle.ViewModel
import com.group2.sinow.data.local.UserPreferenceDataSource

class AccountFeatureViewModel(
    private val userPreferenceDataSource: UserPreferenceDataSource
) : ViewModel() {

    val userDarkMode = userPreferenceDataSource.getUserDarkModePrefFlow()
}
