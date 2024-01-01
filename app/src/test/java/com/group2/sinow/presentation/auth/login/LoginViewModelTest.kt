package com.group2.sinow.presentation.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.local.UserPreferenceDataSource
import com.group2.sinow.data.network.api.model.login.LoginData
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.tools.MainCoroutineRule
import com.group2.sinow.tools.getOrAwaitValue
import com.group2.sinow.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.Exception

class LoginViewModelTest {

    @MockK
    private lateinit var repository: AuthRepository

    @MockK
    private lateinit var userPreferenceDataSource: UserPreferenceDataSource

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(
        UnconfinedTestDispatcher()
    )
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = spyk(
            LoginViewModel(repository, userPreferenceDataSource),
            recordPrivateCalls = true
        )
        coEvery { repository.doLogin(any(), any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    LoginResponse("Success", "Berhasil Login", LoginData("token"))
                )
            )
        }

        coEvery { userPreferenceDataSource.getUserTokenFlow() } returns flow {
            emit("token")
        }
    }

    @Test
    fun `doLogin, return success`() {
        runTest {
            loginViewModel.doLogin("rglbdarto@gmail.com", "12345678")
            coVerify { repository.doLogin(any(), any()) }
            val actualLoginResult = loginViewModel.loginResult.getOrAwaitValue()
            assertTrue(actualLoginResult is ResultWrapper.Success)
        }
    }

    @Test
    fun `doLogin, return failed`() {
        val username = "username"
        val password = "password"

        coEvery { repository.doLogin(any(), any()) } returns flowOf(
            ResultWrapper.Error(
                Exception("Your email or password is incorrect"),
                null
            )
        )

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
}
