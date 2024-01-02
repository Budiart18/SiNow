package com.group2.sinow.presentation.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.tools.MainCoroutineRule
import com.group2.sinow.tools.getOrAwaitValue
import com.group2.sinow.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RegisterViewModelTest {

    @MockK
    private lateinit var repository: AuthRepository

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(
        UnconfinedTestDispatcher()
    )

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            RegisterViewModel(repository),
            recordPrivateCalls = true
        )
        coEvery { repository.registerUser(any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    RegisterResponse("registrasi berhasil", "Sukses")
                )
            )
        }
    }

    @Test
    fun `test register user`() {
        runTest {
            val mockRegisterRequest = mockk<RegisterRequest>(relaxed = true)
            viewModel.register(mockRegisterRequest)
            coVerify { repository.registerUser(any()) }
            val result = viewModel.registerState.getOrAwaitValue()
            assertTrue(result is ResultWrapper.Success)
        }
    }
}
