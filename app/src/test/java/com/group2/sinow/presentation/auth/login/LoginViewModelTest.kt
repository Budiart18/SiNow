package com.group2.sinow.presentation.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.local.UserPreferenceDataSource
import com.group2.sinow.data.network.api.model.login.LoginData
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.helper.MainCoroutineRule
import com.group2.sinow.utils.ResultWrapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class LoginViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private val repository = mockk<AuthRepository>()
    private val userPreferenceDataSource = mockk<UserPreferenceDataSource>()
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository, userPreferenceDataSource)
        coEvery { repository.doLogin(any(), any()) } returns flowOf(ResultWrapper.Success(
            LoginResponse("Success", "Berhasil Login", LoginData("token"))
        ))

        coEvery { userPreferenceDataSource.getUserTokenFlow() } returns flowOf("token")
    }

    @Test
    fun `doLogin, return success`() {
        val username = "username"
        val password = "password"

        loginViewModel.doLogin(username, password)

        val actualLoginResult = loginViewModel.loginResult.value
        assert(actualLoginResult != null)
    }

    @Test
    fun `doLogin, return failed`() {
        val username = "username"
        val password = "password"

        coEvery { repository.doLogin(any(), any()) } returns flowOf(ResultWrapper.Error(
            Exception("Your email or password is incorrect"), null))

        loginViewModel.doLogin(username, password)

        val actualLoginResult = loginViewModel.userToken
        assertEquals(null, actualLoginResult.value)
    }

    @Test
    fun `doLogin, return empty`() {
        val username = "username"
        val password = "password"

        coEvery { repository.doLogin(any(), any()) } returns flowOf(ResultWrapper.Empty(null))

        loginViewModel.doLogin(username, password)

        val actualLoginResult = loginViewModel.userToken
        assertEquals(null, actualLoginResult.value)
    }

    @Test
    fun `saveToken, return success`() {
        val token = "test_token"

        // Mock the saveUserToken method
        coEvery { userPreferenceDataSource.saveUserToken(token) } just Runs

        // Call the saveToken method
        loginViewModel.saveToken(token)

        // Verify that the saveUserToken method was called with the test token
        coVerify { userPreferenceDataSource.saveUserToken(token) }
    }
}