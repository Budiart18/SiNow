package com.group2.sinow.data.network.api.datasource

import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.changepassword.ChangePasswordResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.detailcourse.DataResponse
import com.group2.sinow.data.network.api.model.followcourse.FollowCourseResponse
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.notification.DeleteNotificationResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.model.profile.ProfileResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpRequest
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordRequest
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordResponse
import com.group2.sinow.data.network.api.model.transaction.TransactionResponse
import com.group2.sinow.data.network.api.model.transactionhistory.DeleteTransactionResponse
import com.group2.sinow.data.network.api.model.transactionhistory.TransactionDetailResponse
import com.group2.sinow.data.network.api.model.transactionhistory.TransactionsHistoryResponse
import com.group2.sinow.data.network.api.model.updateprofile.UpdateUserDataResponse
import com.group2.sinow.data.network.api.model.userclass.ClassesResponse
import com.group2.sinow.data.network.api.model.usermodule.UserModuleDataResponse
import com.group2.sinow.data.network.api.model.verifyemail.Data
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
import com.group2.sinow.data.network.api.service.SinowApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SinowApiDataSourceTest {

    @MockK
    lateinit var service: SinowApiService

    private lateinit var dataSource: SinowDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = SinowApiDataSource(service)
    }

    @Test
    fun `doLogin should return LoginResponse`() {
        // Arrange
        val loginRequest = LoginRequest("username", "password")
        val expectedResponse = LoginResponse("success", "Logged in successfully", null)

        coEvery { service.doLogin(loginRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.doLogin(loginRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `doLogin should return failed LoginResponse`() {
        // Arrange
        val loginRequest = LoginRequest("username", "password")
        val expectedResponse = LoginResponse("failed", "email atau password salah", null)

        coEvery { service.doLogin(loginRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.doLogin(loginRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `registerUser should return RegisterResponse`() {
        // Arrange
        val registerRequest =
            RegisterRequest("username", "user@gmail.com", "08123456789", "12345678")
        val expectedResponse = RegisterResponse(
            "Success",
            "Registrasi berhasil & OTP berhasil dikirim ke email anda, silahkan verifikasi OTP sebelum login"
        )

        coEvery { service.registerUser(registerRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.registerUser(registerRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `registerUser should return failed RegisterResponse`() {
        // Arrange
        val registerRequest =
            RegisterRequest("username", "user@gmail.com", "08123456789", "12345678")
        val expectedResponse = RegisterResponse(
            "Error",
            "Data registrasi tidak valid: email tidak valid, nomor telepon tidak valid, panjang password kurang dari 8 karakter, email sudah terdaftar, nomor telepon sudah terdaftar"
        )

        coEvery { service.registerUser(registerRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.registerUser(registerRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `verifyEmail should return success VerifyEmailResponse`() {
        // Arrange
        val otpRequest = VerifyEmailRequest("user@gmail.com", "123456")
        val expectedResponse = VerifyEmailResponse(
            Data("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwibmFtZSI6IkZhZGhsYW4iLCJyb2xlIjoiYWRtaW4iLCJpYXQiOjE3MDA4NzY0NTksImV4cCI6MTcwMDk2Mjg1OSwiaXNzIjoiU2lOb3dfU2VjdXJpdHkifQ.eoFRELxyGri_tV4-ZltzZxHPGz6bcwwSq2zLPN61Mng"),
            "Berhasil verifikasi email",
            "Success"
        )

        coEvery { service.verifyEmail(otpRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.verifyEmail(otpRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `verifyEmail should return failed VerifyEmailResponse`() {
        // Arrange
        val otpRequest = VerifyEmailRequest("user@gmail.com", "123456")
        val expectedResponse =
            VerifyEmailResponse(null, "Data verifikasi email tidak valid", "Error")

        coEvery { service.verifyEmail(otpRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.verifyEmail(otpRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `resendOtp should return success`() {
        // Arrange
        val resendOtpRequest = ResendOtpRequest("user@gmail.com")
        val expectedResponse = ResendOtpResponse("Success", "OTP berhasil dikirim ke email anda")

        coEvery { service.resendOtp(resendOtpRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.resendOtp(resendOtpRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `resendOtp should return failed`() {
        // Arrange
        val resendOtpRequest = ResendOtpRequest("user@gmail.com")
        val expectedResponse = ResendOtpResponse("Failed", "Data resend OTP tidak valid")

        coEvery { service.resendOtp(resendOtpRequest) } returns expectedResponse

        // Act
        val actualResponse = runBlocking { dataSource.resendOtp(resendOtpRequest) }

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `resetPassword gets called`() {
        runTest {
            val mockResponse = mockk<ResetPasswordResponse>()
            coEvery { service.resetPassword(any()) } returns mockResponse
            val response = dataSource.resetPassword(ResetPasswordRequest("user@gmail.com"))
            coVerify { service.resetPassword(any()) } // memverifikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getCategories gets called`() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>()
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() } // memverifikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getUserCourse gets called`() {
        runTest {
            val mockResponse = mockk<ClassesResponse>()
            coEvery { service.getUserCourses(any(), any()) } returns mockResponse
            val response = dataSource.getUserCourse("search", "progress")
            coVerify {
                service.getUserCourses(
                    any(),
                    any()
                )
            } // memverifikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getCourses gets called`() {
        runTest {
            val mockResponse = mockk<CoursesResponse>()
            coEvery { service.getCourses() } returns mockResponse
            val response = dataSource.getCourses()
            coVerify { service.getCourses() } // memverifikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getCoursesFilter gets called`() {
        runTest {
            val mockResponse = mockk<CoursesResponse>()
            coEvery {
                service.getCoursesFilter(
                    search = any(),
                    type = any(),
                    category = any(),
                    level = any(),
                    sortBy = any()
                )
            } returns mockResponse

            val response = dataSource.getCoursesFilter(
                search = "search",
                type = "type",
                category = listOf(1, 2, 3),
                level = listOf("level1", "level2", "level3"),
                sortBy = "sortBy"
            )

            coVerify {
                service.getCoursesFilter(
                    search = any(),
                    type = any(),
                    category = any(),
                    level = any(),
                    sortBy = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getNotification gets called`() {
        runTest {
            val mockResponse = mockk<NotificationResponse>()
            coEvery { service.getNotifications() } returns mockResponse

            val response = dataSource.getNotification()

            coVerify { service.getNotifications() } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getNotificationDetail gets called`() {
        runTest {
            val mockResponse = mockk<NotificationDetailResponse>()
            coEvery {
                service.getNotificationDetail(
                    id = any()
                )
            } returns mockResponse

            val response = dataSource.getNotificationDetail(
                id = 1
            )

            coVerify {
                service.getNotificationDetail(
                    id = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `deleteNotification gets called`() {
        runTest {
            val mockResponse = mockk<DeleteNotificationResponse>()
            coEvery {
                service.deleteNotification(
                    id = any()
                )
            } returns mockResponse

            val response = dataSource.deleteNotification(
                id = 1
            )

            coVerify {
                service.deleteNotification(
                    id = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getDetailCourse gets called`() {
        runTest {
            val mockResponse = mockk<DataResponse>()
            coEvery {
                service.getCourseDetail(
                    id = any()
                )
            } returns mockResponse

            val response = dataSource.getDetailCourse(
                id = 1
            )

            coVerify {
                service.getCourseDetail(
                    id = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getUserModuleData gets called`() {
        runTest {
            val mockResponse = mockk<UserModuleDataResponse>()
            coEvery {
                service.getUserModuleData(
                    courseId = any(),
                    userModuleId = any()
                )
            } returns mockResponse

            val response = dataSource.getUserModuleData(
                courseId = 1,
                userModuleId = 1
            )

            coVerify {
                service.getUserModuleData(
                    courseId = any(),
                    userModuleId = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getUserData gets called`() {
        runTest {
            val mockResponse = mockk<ProfileResponse>()
            coEvery { service.getUserData() } returns mockResponse

            val response = dataSource.getUserData()

            coVerify { service.getUserData() } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getUserTransactionHistory gets called`() {
        runTest {
            val mockResponse = mockk<TransactionsHistoryResponse>()
            coEvery { service.getUserTransactionHistory() } returns mockResponse

            val response = dataSource.getUserTransactionHistory()

            coVerify { service.getUserTransactionHistory() } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `getUserDetailTransaction gets called`() {
        runTest {
            val mockResponse = mockk<TransactionDetailResponse>()
            coEvery {
                service.getUserDetailTransaction(
                    id = any()
                )
            } returns mockResponse

            val response = dataSource.getUserDetailTransaction(
                id = "1"
            )

            coVerify {
                service.getUserDetailTransaction(
                    id = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `updateUserData gets called`() {
        runTest {
            val mockResponse = mockk<UpdateUserDataResponse>()
            coEvery {
                service.updateUserData(
                    name = any(),
                    phoneNumber = any(),
                    country = any(),
                    city = any(),
                    image = any()
                )
            } returns mockResponse

            val response = dataSource.updateUserData(
                name = mockk(),
                phoneNumber = mockk(),
                country = mockk(),
                city = mockk(),
                image = mockk()
            )

            coVerify {
                service.updateUserData(
                    name = any(),
                    phoneNumber = any(),
                    country = any(),
                    city = any(),
                    image = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `changePassword gets called`() {
        runTest {
            val mockResponse = mockk<ChangePasswordResponse>()
            coEvery {
                service.changePassword(
                    changePasswordRequest = any()
                )
            } returns mockResponse

            val response = dataSource.changePassword(
                changePasswordRequest = mockk()
            )

            coVerify {
                service.changePassword(
                    changePasswordRequest = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `followCourse gets called`() {
        runTest {
            val mockResponse = mockk<FollowCourseResponse>()
            coEvery {
                service.followCourse(
                    courseId = any()
                )
            } returns mockResponse

            val response = dataSource.followCourse(
                courseId = 1
            )

            coVerify {
                service.followCourse(
                    courseId = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `buyPremiumCourse gets called`() {
        runTest {
            val mockResponse = mockk<TransactionResponse>()
            coEvery {
                service.buyPremiumCourse(
                    transactionRequest = any()
                )
            } returns mockResponse

            val response = dataSource.buyPremiumCourse(
                transactionRequest = mockk()
            )

            coVerify {
                service.buyPremiumCourse(
                    transactionRequest = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun `deleteTransaction gets called`() {
        runTest {
            val mockResponse = mockk<DeleteTransactionResponse>()
            coEvery {
                service.deleteTransaction(
                    transactionId = any()
                )
            } returns mockResponse

            val response = dataSource.deleteTransaction(
                transactionId = "1"
            )

            coVerify {
                service.deleteTransaction(
                    transactionId = any()
                )
            } // memverifikasi apakah service sudah terpanggil

            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }
}