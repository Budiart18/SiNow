package com.group2.sinow.data.repository

import app.cash.turbine.test
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.login.LoginData
import com.group2.sinow.data.network.api.model.login.LoginResponse
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.data.network.api.model.register.RegisterResponse
import com.group2.sinow.data.network.api.model.resendotp.ResendOtpResponse
import com.group2.sinow.data.network.api.model.resetpassword.ResetPasswordResponse
import com.group2.sinow.data.network.api.model.verifyemail.Data
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailResponse
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

class AuthRepositoryImplTest {

    @MockK
    lateinit var dataSource: SinowDataSource

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = AuthRepositoryImpl(dataSource)
    }

    @Test
    fun `doLogin, result success`() {
        val fakeUser = LoginResponse(
            status = "Success",
            message = "Berhasil login",
            LoginData(
                "token"
            )
        )
        runTest {
            coEvery { dataSource.doLogin(any()) } returns fakeUser
            repository.doLogin("user@gmail.com", "123456").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.status, "Success")
                coVerify { dataSource.doLogin(any()) }
            }
        }
    }

    @Test
    fun `doLogin, result loading`() {
        val mockUser = mockk<LoginResponse>()
        runTest {
            coEvery { dataSource.doLogin(any()) } returns mockUser
            repository.doLogin("user@gmail.com", "123456").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.doLogin(any()) }
            }
        }
    }

    @Test
    fun `doLogin, result error`() {
        // Arrange
        val exception = Exception("Network error")
        coEvery { dataSource.doLogin(any()) } throws exception

        runTest {
            // Act
            repository.doLogin("user@gmail.com", "123456").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.doLogin(any()) }
            }
        }
    }

    @Test
    fun `doLogin, result empty`() {
        val fakeUser = LoginResponse(
            status = "Failed",
            message = "Email atau password salah",
            null
        )

        runTest {
            coEvery { dataSource.doLogin(any()) } returns fakeUser
            repository.doLogin("user@gmail.com", "123456").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.message, "Email atau password salah")
                coVerify { dataSource.doLogin(any()) }
            }
        }
    }

    @Test
    fun `registerUser, result success`() {
        val fakeUser = RegisterResponse(
            status = "Success",
            message = "Registrasi berhasil & OTP berhasil dikirim ke email anda, silahkan verifikasi OTP sebelum login"
        )
        runTest {
            coEvery { dataSource.registerUser(any()) } returns fakeUser
            repository.registerUser(
                RegisterRequest(
                    "user",
                    "user@gmail.com",
                    "0812345789",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.status, "Success")
                coVerify { dataSource.registerUser(any()) }
            }
        }
    }

    @Test
    fun `registerUser, result loading`() {
        val mockUser = mockk<RegisterResponse>()
        runTest {
            coEvery { dataSource.registerUser(any()) } returns mockUser
            repository.registerUser(
                RegisterRequest(
                    "user",
                    "user@gmail.com",
                    "0812345789",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.registerUser(any()) }
            }
        }
    }

    @Test
    fun `registerUser, result error`() {
        // Arrange
        val exception = Exception("Network error")
        coEvery { dataSource.registerUser(any()) } throws exception

        runTest {
            // Act
            repository.registerUser(
                RegisterRequest(
                    "user",
                    "user@gmail.com",
                    "0812345789",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.registerUser(any()) }
            }
        }
    }

    @Test
    fun `registerUser, email already registered`() {
        val fakeUser = RegisterResponse(
            status = "Failed",
            message = "Email sudah terdaftar"
        )

        runTest {
            coEvery { dataSource.registerUser(any()) } returns fakeUser
            repository.registerUser(
                RegisterRequest(
                    "user",
                    "user@gmail.com",
                    "0812345789",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.message, "Email sudah terdaftar")
                coVerify { dataSource.registerUser(any()) }
            }
        }
    }

    @Test
    fun `verifyEmail, result success`() {
        val fakeResponse = VerifyEmailResponse(
            status = "Success",
            message = "Berhasil verifikasi email",
            data = Data("token")
        )
        runTest {
            coEvery { dataSource.verifyEmail(any()) } returns fakeResponse
            repository.verifyEmail(
                VerifyEmailRequest(
                    "user@gmail.com",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.status, "Success")
                coVerify { dataSource.verifyEmail(any()) }
            }
        }
    }

    @Test
    fun `verifyEmail, result loading`() {
        val mockResponse = mockk<VerifyEmailResponse>()
        runTest {
            coEvery { dataSource.verifyEmail(any()) } returns mockResponse
            repository.verifyEmail(
                VerifyEmailRequest(
                    "user@gmail.com",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.verifyEmail(any()) }
            }
        }
    }

    @Test
    fun `verifyEmail, result error`() {
        // Arrange
        val exception = Exception("Network error")
        coEvery { dataSource.verifyEmail(any()) } throws exception

        runTest {
            // Act
            repository.verifyEmail(
                VerifyEmailRequest(
                    "user@gmail.com",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.verifyEmail(any()) }
            }
        }
    }

    @Test
    fun `verifyEmail, result empty`() {
        val fakeResponse = VerifyEmailResponse(
            status = "Failed",
            message = "Email atau password salah",
            data = null
        )

        runTest {
            coEvery { dataSource.verifyEmail(any()) } returns fakeResponse
            repository.verifyEmail(
                VerifyEmailRequest(
                    "user@gmail.com",
                    "123456"
                )
            ).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.data, null)
                coVerify { dataSource.verifyEmail(any()) }
            }
        }
    }

    @Test
    fun `resendOtp, result success`() {
        val fakeResponse = ResendOtpResponse(
            status = "Success",
            message = "Kode OTP berhasil dikirim ulang ke email"
        )
        runTest {
            coEvery { dataSource.resendOtp(any()) } returns fakeResponse
            repository.resendOtp("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.status, "Success")
                coVerify { dataSource.resendOtp(any()) }
            }
        }
    }

    @Test
    fun `resendOtp, result loading`() {
        val mockResponse = mockk<ResendOtpResponse>()
        runTest {
            coEvery { dataSource.resendOtp(any()) } returns mockResponse
            repository.resendOtp("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.resendOtp(any()) }
            }
        }
    }

    @Test
    fun `resendOtp, result error`() {
        // Arrange
        val exception = Exception("Network error")
        coEvery { dataSource.resendOtp(any()) } throws exception

        runTest {
            // Act
            repository.resendOtp("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.resendOtp(any()) }
            }
        }
    }

    @Test
    fun `resendOtp, result empty`() {
        val fakeResponse = ResendOtpResponse(
            status = "Failed",
            message = "Email tidak terdaftar"
        )

        runTest {
            coEvery { dataSource.resendOtp(any()) } returns fakeResponse
            repository.resendOtp("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.message, "Email tidak terdaftar")
                coVerify { dataSource.resendOtp(any()) }
            }
        }
    }

    @Test
    fun `resetPassword, result success`() {
        val fakeResponse = ResetPasswordResponse(
            email = "user@gmail.com"
        )
        runTest {
            coEvery { dataSource.resetPassword(any()) } returns fakeResponse
            repository.resetPassword("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.email, "user@gmail.com")
                coVerify { dataSource.resetPassword(any()) }
            }
        }
    }

    @Test
    fun `resetPassword, result loading`() {
        val mockResponse = mockk<ResetPasswordResponse>()
        runTest {
            coEvery { dataSource.resetPassword(any()) } returns mockResponse
            repository.resetPassword("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.resetPassword(any()) }
            }
        }
    }

    @Test
    fun `resetPassword, result error`() {
        // Arrange
        val exception = Exception("Network error")
        coEvery { dataSource.resetPassword(any()) } throws exception

        runTest {
            // Act
            repository.resetPassword("user@gmail.com").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()

                // Assert
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.resetPassword(any()) }
            }
        }
    }

    @Test
    fun `resetPassword, result empty`() {
        val fakeResponse = ResetPasswordResponse(
            email = ""
        )

        runTest {
            coEvery { dataSource.resetPassword(any()) } returns fakeResponse
            repository.resetPassword("").map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.email, "")
                coVerify { dataSource.resetPassword(any()) }
            }
        }
    }
}
