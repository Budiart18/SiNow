package com.group2.sinow.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.profile.ProfileData
import com.group2.sinow.tools.MainCoroutineRule
import com.group2.sinow.tools.getOrAwaitValue
import com.group2.sinow.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @MockK
    private lateinit var repository: UserRepository

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(
        UnconfinedTestDispatcher()
    )

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = MainViewModel(repository)
        every { repository.getUserData() } returns flow {
            emit(
                ResultWrapper.Success(
                    ProfileData(1, "farhan", "imgUrl", "country", "city", "user", "createAt", "updateAt", mockk())
                )
            )
        }
    }

    @Test
    fun `test get user data`() {
        runTest {
            val result = viewModel.getUserData()
            val userData = viewModel.userData.getOrAwaitValue()
            assertEquals(result, Unit)
            assertEquals(userData.payload?.id, 1)
            verify { repository.getUserData() }
        }
    }
}
