package com.group2.sinow.data.repository

import app.cash.turbine.test
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordRequest
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordResponse
import com.group2.sinow.data.network.api.model.profile.ProfileDataResponse
import com.group2.sinow.data.network.api.model.profile.ProfileResponse
import com.group2.sinow.data.network.api.model.transactionhistory.DataDetailTransaction
import com.group2.sinow.data.network.api.model.transactionhistory.DataTransactions
import com.group2.sinow.data.network.api.model.transactionhistory.DeleteTransactionResponse
import com.group2.sinow.data.network.api.model.transactionhistory.Transaction
import com.group2.sinow.data.network.api.model.transactionhistory.TransactionDetailResponse
import com.group2.sinow.data.network.api.model.transactionhistory.TransactionsHistoryResponse
import com.group2.sinow.data.network.api.model.updateprofile.DataResponse
import com.group2.sinow.data.network.api.model.updateprofile.UpdateUserDataResponse
import com.group2.sinow.data.network.api.model.userclass.ClassesResponse
import com.group2.sinow.data.network.api.model.userclass.Data
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Before
import org.junit.Test
import java.io.File

class UserRepositoryImplTest {
    @MockK
    lateinit var dataSource: SinowDataSource

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun `getUserData, result success`() {
        val fakeResponse = ProfileResponse(
            status = "Success",
            message = "Berhasil mendapatkan data",
            data = ProfileDataResponse(
                id = 1,
                name = "name",
                country = "country",
                city = "city",
                createdAt = "2021-08-01 00:00:00",
                updatedAt = "2021-08-01 00:00:00",
                photoProfileUrl = "photoProfileUrl",
                role = "role",
                auth = null
            )
        )

        runTest {
            coEvery { dataSource.getUserData() } returns fakeResponse
            repository.getUserData().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.id, 1)
                coVerify { dataSource.getUserData() }
            }
        }
    }

    @Test
    fun `getUserData, result loading`() {
        val fakeResponse = mockk<ProfileResponse>()

        runTest {
            coEvery { dataSource.getUserData() } returns fakeResponse
            repository.getUserData().map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getUserData() }
            }
        }
    }

    @Test
    fun `getUserData, result error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.getUserData() } throws exception

        runTest {
            repository.getUserData().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getUserData() }
            }
        }
    }

    @Test
    fun `getUserData, result empty`() {
        val fakeResponse = ProfileResponse(
            status = "Failed",
            message = "Gagal mendapatkan data",
            data = ProfileDataResponse(
                id = null,
                name = null,
                country = null,
                city = null,
                createdAt = null,
                updatedAt = null,
                photoProfileUrl = null,
                role = null,
                auth = null
            )
        )

        runTest {
            coEvery { dataSource.getUserData() } returns fakeResponse
            repository.getUserData().map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.name, "")
                coVerify { dataSource.getUserData() }
            }
        }
    }

    @Test
    fun `updateUserData, result success`() {
        val fakeResponse = UpdateUserDataResponse(
            status = "Success",
            message = "Berhasil mengubah data",
            data = DataResponse(
                "token"
            )
        )

        // Create RequestBody instances
        val name = "Test Name".toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumber = "1234567890".toRequestBody("text/plain".toMediaTypeOrNull())
        val country = "Test Country".toRequestBody("text/plain".toMediaTypeOrNull())
        val city = "Test City".toRequestBody("text/plain".toMediaTypeOrNull())

        // Create a MultipartBody.Part instance
        val file = File("path/to/your/file")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, requestFile)

        runTest {
            coEvery {
                dataSource.updateUserData(
                    name,
                    phoneNumber,
                    country,
                    city,
                    image
                )
            } returns fakeResponse
            repository.updateUserData(name, phoneNumber, country, city, image).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload, true)
                coVerify { dataSource.updateUserData(name, phoneNumber, country, city, image) }
            }
        }
    }

    @Test
    fun `updateUserData, result loading`() {
        val fakeResponse = mockk<UpdateUserDataResponse>()

        // Create RequestBody instances
        val name = "Test Name".toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumber = "1234567890".toRequestBody("text/plain".toMediaTypeOrNull())
        val country = "Test Country".toRequestBody("text/plain".toMediaTypeOrNull())
        val city = "Test City".toRequestBody("text/plain".toMediaTypeOrNull())

        // Create a MultipartBody.Part instance
        val file = File("path/to/your/file")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, requestFile)

        runTest {
            coEvery {
                dataSource.updateUserData(
                    name,
                    phoneNumber,
                    country,
                    city,
                    image
                )
            } returns fakeResponse
            repository.updateUserData(name, phoneNumber, country, city, image).map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.updateUserData(name, phoneNumber, country, city, image) }
            }
        }
    }

    @Test
    fun `updateUserData, result error`() {
        val exception = Exception("Network error")

        // Create RequestBody instances
        val name = "Test Name".toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumber = "1234567890".toRequestBody("text/plain".toMediaTypeOrNull())
        val country = "Test Country".toRequestBody("text/plain".toMediaTypeOrNull())
        val city = "Test City".toRequestBody("text/plain".toMediaTypeOrNull())

        // Create a MultipartBody.Part instance
        val file = File("path/to/your/file")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, requestFile)

        coEvery {
            dataSource.updateUserData(
                name,
                phoneNumber,
                country,
                city,
                image
            )
        } throws exception

        runTest {
            repository.updateUserData(name, phoneNumber, country, city, image).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.updateUserData(name, phoneNumber, country, city, image) }
            }
        }
    }

    @Test
    fun `updateUserData, result empty`() {
        val fakeResponse = UpdateUserDataResponse(
            status = "Failed",
            message = "Gagal mengubah data",
            data = null
        )

        // Create RequestBody instances
        val name = "Test Name".toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumber = "1234567890".toRequestBody("text/plain".toMediaTypeOrNull())
        val country = "Test Country".toRequestBody("text/plain".toMediaTypeOrNull())
        val city = "Test City".toRequestBody("text/plain".toMediaTypeOrNull())

        // Create a MultipartBody.Part instance
        val file = File("path/to/your/file")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, requestFile)

        runTest {
            coEvery {
                dataSource.updateUserData(
                    name,
                    phoneNumber,
                    country,
                    city,
                    image
                )
            } returns fakeResponse
            repository.updateUserData(name, phoneNumber, country, city, image).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()

                assertEquals(data.payload, false)
                coVerify { dataSource.updateUserData(name, phoneNumber, country, city, image) }
            }
        }
    }

    @Test
    fun `changePassword, result success`() {
        val fakeResponse = ChangePasswordResponse(
            status = "Success",
            message = "Berhasil mengubah password"
        )

        runTest {
            coEvery {
                dataSource.changePassword(
                    ChangePasswordRequest(
                        "oldPassword",
                        "newPassword",
                        "newPassword"
                    )
                )
            } returns fakeResponse
            repository.changePassword("oldPassword", "newPassword", "newPassword").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload, true)
                coVerify {
                    dataSource.changePassword(
                        ChangePasswordRequest(
                            "oldPassword",
                            "newPassword",
                            "newPassword"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `changePassword, result loading`() {
        val fakeResponse = mockk<ChangePasswordResponse>()

        runTest {
            coEvery {
                dataSource.changePassword(
                    ChangePasswordRequest(
                        "oldPassword",
                        "newPassword",
                        "newPassword"
                    )
                )
            } returns fakeResponse
            repository.changePassword("oldPassword", "newPassword", "newPassword").map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify {
                    dataSource.changePassword(
                        ChangePasswordRequest(
                            "oldPassword",
                            "newPassword",
                            "newPassword"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `changePassword, result error`() {
        val exception = Exception("Network error")
        coEvery {
            dataSource.changePassword(
                ChangePasswordRequest(
                    "oldPassword",
                    "newPassword",
                    "newPassword"
                )
            )
        } throws exception

        runTest {
            repository.changePassword("oldPassword", "newPassword", "newPassword").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify {
                    dataSource.changePassword(
                        ChangePasswordRequest(
                            "oldPassword",
                            "newPassword",
                            "newPassword"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `changePassword, result empty`() {
        val fakeResponse = ChangePasswordResponse(
            status = "Failed",
            message = "Gagal mengubah password"
        )

        runTest {
            coEvery {
                dataSource.changePassword(
                    ChangePasswordRequest(
                        "oldPassword",
                        "newPassword",
                        "newPassword"
                    )
                )
            } returns fakeResponse
            repository.changePassword("oldPassword", "newPassword", "newPassword").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                coVerify {
                    dataSource.changePassword(
                        ChangePasswordRequest(
                            "oldPassword",
                            "newPassword",
                            "newPassword"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `getUserCourses, result success`() {
        val fakeResponse = ClassesResponse(
            status = "Success",
            message = "Berhasil mendapatkan data",
            data = listOf(
                Data(
                    id = 1,
                    createdAt = "2021-08-01 00:00:00",
                    updatedAt = "2021-08-01 00:00:00",
                    userId = 1,
                    courseId = 1,
                    isAccessible = true,
                    lastSeen = "2021-08-01 00:00:00",
                    progress = "progress",
                    progressPercentage = 1,
                    course = null
                )
            )
        )

        runTest {
            coEvery { dataSource.getUserCourse() } returns fakeResponse
            repository.getUserCourses().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                coVerify { dataSource.getUserCourse() }
            }
        }
    }

    @Test
    fun `getUserCourses, result loading`() {
        val fakeResponse = mockk<ClassesResponse>()

        runTest {
            coEvery { dataSource.getUserCourse() } returns fakeResponse
            repository.getUserCourses().map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getUserCourse() }
            }
        }
    }

    @Test
    fun `getUserCourses, result error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.getUserCourse() } throws exception

        runTest {
            repository.getUserCourses().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getUserCourse() }
            }
        }
    }

    @Test
    fun `getUserCourses, result empty`() {
        val fakeResponse = ClassesResponse(
            status = "Failed",
            message = "Gagal mendapatkan data",
            data = null
        )

        runTest {
            coEvery { dataSource.getUserCourse() } returns fakeResponse
            repository.getUserCourses().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.size, 0)
                coVerify { dataSource.getUserCourse() }
            }
        }
    }

    @Test
    fun `getUserTransactionHistory, result success`() {
        val fakeResponse = TransactionsHistoryResponse(
            status = "Success",
            message = "Berhasil mendapatkan data",
            data = DataTransactions(
                transactions = listOf(
                    Transaction(
                        id = "id",
                        createdAt = "2021-08-01 00:00:00",
                        updatedAt = "2021-08-01 00:00:00",
                        userId = 1,
                        courseId = 1,
                        paymentMethod = "paymentMethod",
                        paymentUrl = "paymentUrl",
                        course = null,
                        coursePrice = 1,
                        discountPrice = 1,
                        taxPrice = 1,
                        totalPrice = 1,
                        promoDiscountPercentage = 1,
                        taxPercentage = 1,
                        status = "status",
                        paidAt = "2021-08-01 00:00:00",
                        expiredAt = "2021-08-01 00:00:00",
                        noOrder = "noOrder"
                    )
                )
            )
        )

        runTest {
            coEvery { dataSource.getUserTransactionHistory() } returns fakeResponse
            repository.getUserTransactionHistory().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                coVerify { dataSource.getUserTransactionHistory() }
            }
        }
    }

    @Test
    fun `TransactionsHistoryResponse, result loading`() {
        val fakeResponse = mockk<TransactionsHistoryResponse>()

        runTest {
            coEvery { dataSource.getUserTransactionHistory() } returns fakeResponse
            repository.getUserTransactionHistory().map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getUserTransactionHistory() }
            }
        }
    }

    @Test
    fun `TransactionsHistoryResponse, result error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.getUserTransactionHistory() } throws exception

        runTest {
            repository.getUserTransactionHistory().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getUserTransactionHistory() }
            }
        }
    }

    @Test
    fun `TransactionsHistoryResponse, result empty`() {
        val fakeResponse = TransactionsHistoryResponse(
            status = "Failed",
            message = "Gagal mendapatkan data",
            data = null
        )

        runTest {
            coEvery { dataSource.getUserTransactionHistory() } returns fakeResponse
            repository.getUserTransactionHistory().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.size, 0)
                coVerify { dataSource.getUserTransactionHistory() }
            }
        }
    }

    @Test
    fun `getUserDetailTransaction, result success`() {
        val fakeResponse = TransactionDetailResponse(
            status = "Success",
            message = "Berhasil mendapatkan data",
            data = DataDetailTransaction(
                Transaction(
                    id = "id",
                    createdAt = "2021-08-01 00:00:00",
                    updatedAt = "2021-08-01 00:00:00",
                    userId = 1,
                    courseId = 1,
                    paymentMethod = "paymentMethod",
                    paymentUrl = "paymentUrl",
                    course = null,
                    coursePrice = 1,
                    discountPrice = 1,
                    taxPrice = 1,
                    totalPrice = 1,
                    promoDiscountPercentage = 1,
                    taxPercentage = 1,
                    status = "status",
                    paidAt = "2021-08-01 00:00:00",
                    expiredAt = "2021-08-01 00:00:00",
                    noOrder = "noOrder"
                )
            )
        )

        runTest {
            coEvery { dataSource.getUserDetailTransaction("id") } returns fakeResponse
            repository.getUserDetailTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.id, "id")
                coVerify { dataSource.getUserDetailTransaction("id") }
            }
        }
    }

    @Test
    fun `getUserDetailTransaction, result loading`() {
        val fakeResponse = mockk<TransactionDetailResponse>()

        runTest {
            coEvery { dataSource.getUserDetailTransaction("id") } returns fakeResponse
            repository.getUserDetailTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getUserDetailTransaction("id") }
            }
        }
    }

    @Test
    fun `getUserDetailTransaction, result error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.getUserDetailTransaction("id") } throws exception

        runTest {
            repository.getUserDetailTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getUserDetailTransaction("id") }
            }
        }
    }

    @Test
    fun `getUserDetailTransaction, result empty`() {
        val fakeResponse = TransactionDetailResponse(
            status = "Failed",
            message = "Gagal mendapatkan data",
            data = DataDetailTransaction(
                Transaction(
                    id = "",
                    createdAt = "",
                    updatedAt = "",
                    userId = 0,
                    courseId = 0,
                    paymentMethod = "",
                    paymentUrl = "",
                    course = null,
                    coursePrice = 0,
                    discountPrice = 0,
                    taxPrice = 0,
                    totalPrice = 0,
                    promoDiscountPercentage = 0,
                    taxPercentage = 0,
                    status = "",
                    paidAt = "",
                    expiredAt = "",
                    noOrder = ""
                )
            )
        )

        runTest {
            coEvery { dataSource.getUserDetailTransaction("id") } returns fakeResponse
            repository.getUserDetailTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.id, "")
                coVerify { dataSource.getUserDetailTransaction("id") }
            }
        }
    }

    @Test
    fun `deleteTransaction, result success`() {
        val fakeResponse = DeleteTransactionResponse(
            status = "Success",
            message = "Berhasil menghapus data"
        )

        runTest {
            coEvery { dataSource.deleteTransaction("id") } returns fakeResponse
            repository.deleteTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload, true)
                coVerify { dataSource.deleteTransaction("id") }
            }
        }
    }

    @Test
    fun `deleteTransaction, result loading`() {
        val fakeResponse = mockk<DeleteTransactionResponse>()

        runTest {
            coEvery { dataSource.deleteTransaction("id") } returns fakeResponse
            repository.deleteTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(120)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.deleteTransaction("id") }
            }
        }
    }

    @Test
    fun `deleteTransaction, result error`() {
        val exception = Exception("Network error")
        coEvery { dataSource.deleteTransaction("id") } throws exception

        runTest {
            repository.deleteTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.deleteTransaction("id") }
            }
        }
    }

    @Test
    fun `deleteTransaction, result empty`() {
        val fakeResponse = DeleteTransactionResponse(
            status = "Failed",
            message = "Gagal menghapus data"
        )

        runTest {
            coEvery { dataSource.deleteTransaction("id") } returns fakeResponse
            repository.deleteTransaction("id").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertEquals(data.payload, false)
                coVerify { dataSource.deleteTransaction("id") }
            }
        }
    }
}
