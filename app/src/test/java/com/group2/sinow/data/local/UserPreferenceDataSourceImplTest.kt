package com.group2.sinow.data.local

import com.group2.sinow.utils.PreferenceDataStoreHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserPreferenceDataSourceImplTest {
    @MockK
    lateinit var preferenceHelper: PreferenceDataStoreHelper

    private lateinit var userPreferenceDataSource: UserPreferenceDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userPreferenceDataSource = UserPreferenceDataSourceImpl(preferenceHelper)
        coEvery { userPreferenceDataSource.getUserTokenFlow() } returns flow {
            emit("token")
        }
    }

    @Test
    fun `saveUserToken should save token to preference`() {
        // Arrange
        val token = "test_token"
        coEvery {
            preferenceHelper.putPreference(
                UserPreferenceDataSourceImpl.PREF_USER_TOKEN,
                token
            )
        } returns Unit

        // Act
        runBlocking { userPreferenceDataSource.saveUserToken(token) }

        // Assert
        coVerify {
            preferenceHelper.putPreference(
                UserPreferenceDataSourceImpl.PREF_USER_TOKEN,
                token
            )
        }
    }

    @Test
    fun `getUserTokenFlow should return token flow`() {
        // Arrange
        val token = "test_token"
        coEvery {
            preferenceHelper.getPreference(
                UserPreferenceDataSourceImpl.PREF_USER_TOKEN,
                ""
            )
        } returns flowOf(token)

        // Act
        val result = runBlocking { userPreferenceDataSource.getUserTokenFlow().first() }

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `getUserToken should return token`() {
        // Arrange
        val token = "test_token"
        coEvery {
            preferenceHelper.getFirstPreference(
                UserPreferenceDataSourceImpl.PREF_USER_TOKEN,
                ""
            )
        } returns token

        // Act
        val result = runBlocking { userPreferenceDataSource.getUserToken() }

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `deleteToken should delete token from preference`() {
        // Arrange
        coEvery { preferenceHelper.removePreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN) } returns Unit

        // Act
        runBlocking { userPreferenceDataSource.deleteToken() }

        // Assert
        coVerify { preferenceHelper.removePreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN) }
    }

    @Test
    fun getUserDarkModePref() {
        runTest {
            coEvery { preferenceHelper.getFirstPreference(any(), false) } returns true
            val result = userPreferenceDataSource.getUserDarkModePref()
            assertTrue(result)
            coVerify { preferenceHelper.getFirstPreference(any(), false) }
        }
    }

    @Test
    fun getUserDarkModePrefFlow() {
        runTest {
            every { preferenceHelper.getPreference(any(), false) } returns flow {
                emit(true)
            }
            val result = userPreferenceDataSource.getUserDarkModePrefFlow()
            assertTrue(result.first())
            verify { preferenceHelper.getPreference(any(), false) }
        }
    }

    @Test
    fun setUserDarkModePref() {
        runTest {
            coEvery { preferenceHelper.putPreference(any(), true) } returns Unit
            val result = userPreferenceDataSource.setUserDarkModePref(true)
            assertEquals(result, Unit)
            coVerify { preferenceHelper.putPreference(any(), true) }
        }
    }

    @Test
    fun getShouldShowIntroPage() {
        runTest {
            every { preferenceHelper.getPreference(any(), true) } returns flow {
                emit(true)
            }
            val result = userPreferenceDataSource.getShouldShowIntroPage()
            assertTrue(result.first())
            verify { preferenceHelper.getPreference(any(), true) }
        }
    }

    @Test
    fun setShouldShowIntroPage() {
        runTest {
            coEvery { preferenceHelper.putPreference(any(), true) } returns Unit
            val result = userPreferenceDataSource.setShouldShowIntroPage(true)
            assertEquals(result, Unit)
            coVerify { preferenceHelper.putPreference(any(), true) }
        }
    }
}
