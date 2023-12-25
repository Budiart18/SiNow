package com.group2.sinow.utils


import com.group2.sinow.data.local.UserPreferenceDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PreferenceDataStoreHelperImplTest {

    private val preferenceHelper = mockk<PreferenceDataStoreHelper>()
    private val dataSource = UserPreferenceDataSourceImpl(preferenceHelper)
    private val token = "test_token"

    @Before
    fun setup() {
        coEvery { preferenceHelper.putPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, token) } returns Unit
        coEvery { preferenceHelper.getPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, "") } returns flowOf(token)
        coEvery { preferenceHelper.getFirstPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, "") } returns token
        coEvery { preferenceHelper.removePreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN) } returns Unit
    }

    @Test
    fun `saveUserToken should save token to preference`() = runTest {
        // Act
        dataSource.saveUserToken(token)

        // Assert
        coVerify { preferenceHelper.putPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, token) }
    }

    @Test
    fun `getUserTokenFlow should return token flow`() = runTest {

        // Act
        val result = dataSource.getUserTokenFlow().first()

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `getUserToken should return token`() = runTest {
        // Act
        val result = dataSource.getUserToken()

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `deleteToken should delete token from preference`() = runTest {
        // Act
        dataSource.deleteToken()

        // Assert
        coVerify { preferenceHelper.removePreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN) }
    }
}