package com.group2.sinow.presentation.auth.login

import androidx.datastore.preferences.core.stringPreferencesKey
import com.group2.sinow.utils.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource {
    suspend fun saveUserToken(token: String)
    fun getUserTokenFlow(): Flow<String>
    suspend fun getUserToken(): String
    suspend fun deleteToken()
}

class UserPreferenceDataSourceImpl(private val preferenceHelper: PreferenceDataStoreHelper) : UserPreferenceDataSource {

    override suspend fun saveUserToken(token: String) {
        return preferenceHelper.putPreference(PREF_USER_TOKEN, token)
    }

    override fun getUserTokenFlow(): Flow<String> {
        return preferenceHelper.getPreference(PREF_USER_TOKEN, "")
    }

    override suspend fun getUserToken(): String {
        return preferenceHelper.getFirstPreference(PREF_USER_TOKEN, "")
    }

    override suspend fun deleteToken() {
        return preferenceHelper.removePreference(PREF_USER_TOKEN)
    }


    companion object {
        val PREF_USER_TOKEN = stringPreferencesKey("PREF_USER_TOKEN")
    }

}