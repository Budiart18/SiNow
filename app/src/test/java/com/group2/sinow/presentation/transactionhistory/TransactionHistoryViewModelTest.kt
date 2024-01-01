package com.group2.sinow.presentation.transactionhistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.repository.UserRepository
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TransactionHistoryViewModelTest {

    @MockK
    private lateinit var repository: UserRepository

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    private lateinit var viewModel: TransactionHistoryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(TransactionHistoryViewModel(repository))
        coEvery { repository.getUserTransactionHistory() } returns flow {
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
    fun getUserTransaction() {
        viewModel.getUserTransaction()
        val result = viewModel.transaction.getOrAwaitValue()
        assertTrue(result is ResultWrapper.Success)
        assertEquals(result.payload?.size, 3)
        coVerify { repository.getUserTransactionHistory() }
    }
}
