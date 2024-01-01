package com.group2.sinow.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.group2.sinow.utils.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource {
    suspend fun saveUserToken(token: String)
    fun getUserTokenFlow(): Flow<String>
    suspend fun getUserToken(): String
    suspend fun deleteToken()
    suspend fun getUserDarkModePref(): Boolean
    fun getUserDarkModePrefFlow(): Flow<Boolean>
    suspend fun setUserDarkModePref(isUsingDarkMode: Boolean)

    suspend fun clearUserPreferencesTheme()

    suspend fun setShouldShowIntroPage(isFirstTime: Boolean)

    fun getShouldShowIntroPage(): Flow<Boolean>
}

class UserPreferenceDataSourceImpl(private val preferenceHelper: PreferenceDataStoreHelper) :
    UserPreferenceDataSource {

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

    override suspend fun setShouldShowIntroPage(isFirstTime: Boolean) {
        return preferenceHelper.putPreference(PREF_SHOW_INTRO_PAGE, isFirstTime)
    }

    override fun getShouldShowIntroPage(): Flow<Boolean> {
        return preferenceHelper.getPreference(PREF_SHOW_INTRO_PAGE, true)
    }

    override suspend fun getUserDarkModePref(): Boolean {
        return preferenceHelper.getFirstPreference(PREF_USER_DARK_MODE, false)
    }

    override fun getUserDarkModePrefFlow(): Flow<Boolean> {
        return preferenceHelper.getPreference(PREF_USER_DARK_MODE, false)
    }

    override suspend fun setUserDarkModePref(isUsingDarkMode: Boolean) {
        return preferenceHelper.putPreference(PREF_USER_DARK_MODE, isUsingDarkMode)
    }

    override suspend fun clearUserPreferencesTheme() {
        return preferenceHelper.removePreference(PREF_USER_DARK_MODE)
    }

    companion object {
        val PREF_USER_TOKEN = stringPreferencesKey("PREF_USER_TOKEN")
        val PREF_SHOW_INTRO_PAGE = booleanPreferencesKey("PREF_SHOW_INTRO_PAGE")
        val PREF_USER_DARK_MODE = booleanPreferencesKey("PREF_USER_DARK_MODE")
    }
}
