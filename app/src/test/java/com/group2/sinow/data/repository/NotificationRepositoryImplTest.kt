package com.group2.sinow.data.repository

import app.cash.turbine.test
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationItemResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NotificationRepositoryImplTest {
    @MockK
    lateinit var dataSource: SinowDataSource

    private lateinit var repository: NotificationRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = NotificationRepositoryImpl(dataSource)
    }

    @Test
    fun `getNotification, return success`() {
        val fakeResponse = NotificationResponse(
            status = "Success",
            data = listOf(
                NotificationItemResponse(
                    id = 1,
                    type = "type",
                    title = "title",
                    content = "content",
                    userId = 1,
                    isRead = false,
                    createdAt = "2021-08-01 00:00:00",
                    updatedAt = "2021-08-01 00:00:00"
                ),
                NotificationItemResponse(
                    id = 2,
                    type = "type",
                    title = "title",
                    content = "content",
                    userId = 1,
                    isRead = false,
                    createdAt = "2021-08-01 00:00:00",
                    updatedAt = "2021-08-01 00:00:00"
                )
            )
        )

        runTest {
            coEvery { dataSource.getNotification() } returns fakeResponse
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 2)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `getNotification, return loading`() {
        val fakeResponse = mockk<NotificationResponse>()

        runTest {
            coEvery { dataSource.getNotification() } returns fakeResponse
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `getNotification, return empty`() {
        val fakeResponse = NotificationResponse(
            status = "Failed",
            data = null
        )

        runTest {
            coEvery { dataSource.getNotification() } returns fakeResponse
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertEquals(data.payload, null)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `getNotification, return error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.getNotification() } throws exception

        runTest {
            // Act
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `getNotificationDetail, return success`() {
        val fakeResponse = NotificationDetailResponse(
            status = "Success",
            message = "Berhasil mendapatkan data notifikasi",
            data = NotificationItemResponse(
                id = 1,
                type = "type",
                title = "title",
                content = "content",
                userId = 1,
                isRead = false,
                createdAt = "2021-08-01 00:00:00",
                updatedAt = "2021-08-01 00:00:00"
            )
        )

        runTest {
            coEvery { dataSource.getNotificationDetail(1) } returns fakeResponse
            repository.getNotificationDetail(1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.id, 1)
                coVerify { dataSource.getNotificationDetail(1) }
            }
        }
    }

    @Test
    fun `getNotificationDetail, return loading`() {
        val fakeResponse = mockk<NotificationDetailResponse>()

        runTest {
            coEvery { dataSource.getNotificationDetail(1) } returns fakeResponse
            repository.getNotificationDetail(1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getNotificationDetail(1) }
            }
        }
    }

    @Test
    fun `getNotificationDetail, return empty`() {
        val fakeResponse = NotificationDetailResponse(
            status = "Failed",
            message = "Tidak ada notifikasi",
            data = NotificationItemResponse(
                id = null,
                type = null,
                title = null,
                content = null,
                userId = null,
                isRead = null,
                createdAt = null,
                updatedAt = null
            )
        )

        runTest {
            coEvery { dataSource.getNotificationDetail(1) } returns fakeResponse
            repository.getNotificationDetail(1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertEquals(data.payload, null)
                coVerify { dataSource.getNotificationDetail(1) }
            }
        }
    }

    @Test
    fun `getNotificationDetail, return error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.getNotificationDetail(any()) } throws exception

        runTest {
            // Act
            repository.getNotificationDetail(1).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getNotificationDetail(any()) }
            }
        }
    }

    @Test
    fun `deleteNotification, return success`() {
        val fakeResponse = DeleteNotificationResponse(
            status = "Success",
            message = "Berhasil menghapus notifikasi"
        )

        runTest {
            coEvery { dataSource.deleteNotification(1) } returns fakeResponse
            repository.deleteNotification(1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.deleteNotification(1) }
            }
        }
    }

    @Test
    fun `deleteNotification, return loading`() {
        val fakeResponse = mockk<DeleteNotificationResponse>()

        runTest {
            coEvery { dataSource.deleteNotification(1) } returns fakeResponse
            repository.deleteNotification(1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.deleteNotification(1) }
            }
        }
    }

    @Test
    fun `deleteNotification, return error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.deleteNotification(any()) } throws exception

        runTest {
            // Act
            repository.deleteNotification(1).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.deleteNotification(any()) }
            }
        }
    }

    @Test
    fun `deleteNotification, return empty`() {
        val fakeResponse = DeleteNotificationResponse(
            status = "Failed",
            message = "Tidak ada notifikasi"
        )

        runTest {
            coEvery { dataSource.deleteNotification(1) } returns fakeResponse
            repository.deleteNotification(1).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                coVerify { dataSource.deleteNotification(1) }
            }
        }
    }
}
