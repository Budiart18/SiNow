package com.group2.sinow.presentation.notification.notificationlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.repository.NotificationRepository
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NotificationViewModelTest {

    @MockK
    private lateinit var repository: NotificationRepository

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(
        UnconfinedTestDispatcher()
    )

    private lateinit var viewModel: NotificationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = NotificationViewModel(repository)
        every { repository.getNotification() } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }
    }

    @Test
    fun testGetNotifications() {
        val result = viewModel.getNotifications()
        val notifications = viewModel.notifications.getOrAwaitValue()
        assertEquals(result, Unit)
        assertEquals(notifications.payload?.size, 3)
        verify { repository.getNotification() }
    }
}
