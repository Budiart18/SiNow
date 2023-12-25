package com.group2.sinow.data.local

import com.group2.sinow.data.network.api.datasource.SinowApiDataSource
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.service.SinowApiService
import com.group2.sinow.utils.PreferenceDataStoreHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class UserPreferenceDataSourceImplTest {
    @MockK
    lateinit var service: PreferenceDataStoreHelper

    private lateinit var dataSource: UserPreferenceDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = UserPreferenceDataSourceImpl(service)
    }

    @Test
    fun `saveUserToken should save token to preference`() {
        // Arrange
        val token = "test_token"
        coEvery { service.putPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, token) } returns Unit

        // Act
        runBlocking { dataSource.saveUserToken(token) }

        // Assert
        coVerify { service.putPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, token) }
    }

    @Test
    fun `getUserTokenFlow should return token flow`() {
        // Arrange
        val token = "test_token"
        coEvery { service.getPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, "") } returns flowOf(token)

        // Act
        val result = runBlocking { dataSource.getUserTokenFlow().first() }

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `getUserToken should return token`() {
        // Arrange
        val token = "test_token"
        coEvery { service.getFirstPreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN, "") } returns token

        // Act
        val result = runBlocking { dataSource.getUserToken() }

        // Assert
        assertEquals(token, result)
    }

    @Test
    fun `deleteToken should delete token from preference`() {
        // Arrange
        coEvery { service.removePreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN) } returns Unit

        // Act
        runBlocking { dataSource.deleteToken() }

        // Assert
        coVerify { service.removePreference(UserPreferenceDataSourceImpl.PREF_USER_TOKEN) }
    }
}