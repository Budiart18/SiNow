package com.group2.sinow.data.repository

import app.cash.turbine.test
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.category.CategoryItemResponse
import com.group2.sinow.data.network.api.model.course.CourseBenefitResponse
import com.group2.sinow.data.network.api.model.course.CourseCategoryResponse
import com.group2.sinow.data.network.api.model.course.CourseCreatorResponse
import com.group2.sinow.data.network.api.model.course.CourseItemResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.detailcourse.DataResponse
import com.group2.sinow.data.network.api.model.followcourse.FollowCourseResponse
import com.group2.sinow.data.network.api.model.transaction.TransactionDataResponse
import com.group2.sinow.data.network.api.model.transaction.TransactionRequest
import com.group2.sinow.data.network.api.model.transaction.TransactionResponse
import com.group2.sinow.data.network.api.model.usermodule.ModuleResponse
import com.group2.sinow.data.network.api.model.usermodule.UserModuleDataResponse
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


class CourseRepositoryImplTest {

    @MockK
    lateinit var dataSource: SinowDataSource

    private lateinit var repository: CourseRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CourseRepositoryImpl(dataSource)
    }

    @Test
    fun `getCategories, with result success`() {

        val fakeCategoryResponse = CategoriesResponse(
            status = "Success",
            message = "Berhasil mengambil data category",
            data = listOf(
                CategoryItemResponse(
                    id = 1,
                    createdAt = "2021-08-10T09:00:00.000000Z",
                    imageUrl = "https://sinow.id/storage/category/1.png",
                    name = "Teknologi",
                    updatedAt = "2021-08-10T09:00:00.000000Z"
                ),
                CategoryItemResponse(
                    id = 2,
                    imageUrl = "https://sinow.id/storage/category/2.png",
                    name = "Desain",
                    createdAt = "2021-08-10T09:00:00.000000Z",
                    updatedAt = "2021-08-10T09:00:00.000000Z"
                )
            )
        )

        runTest {
            coEvery { dataSource.getCategories() } returns fakeCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 2)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `getCategories, with result error`() {
        runTest {
            coEvery { dataSource.getCategories() } throws IllegalStateException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `getCategories, with result empty`() {
        val fakeCategoryResponse = CategoriesResponse(
            status = "Failed",
            message = "Data category tidak ditemukan",
            data = null
        )

        runTest {
            coEvery { dataSource.getCategories() } returns fakeCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `getCourse, with result success`() {

        val fakeCourseResponse = CoursesResponse(
            status = "Success",
            message = "Berhasil mengambil data course",
            data = listOf(
                CourseItemResponse(
                    id = 1,
                    name = "Belajar Membuat Aplikasi Android untuk Pemula",
                    imageUrl = "https://sinow.id/storage/course/1.png",
                    videoPreviewUrl = "https://sinow.id/storage/course/1.mp4",
                    level = "Pemula",
                    rating = 4.5,
                    categoryId = 1,
                    description = "Belajar Membuat Aplikasi Android untuk Pemula",
                    classCode = "ABC123",
                    totalModule = 10,
                    totalDuration = 100,
                    type = "Free",
                    price = 0,
                    promoDiscountPercentage = 0,
                    totalUser = 100,
                    courseBy = "Sinow",
                    createdBy = 1,
                    createdAt = "2021-08-10T09:00:00.000000Z",
                    updatedAt = "2021-08-10T09:00:00.000000Z",
                    category = CourseCategoryResponse(
                        id = 1,
                        name = "Teknologi",
                    ),
                    courseCreator = CourseCreatorResponse(
                        id = 1,
                        name = "Sinow",
                    ),
                    benefits = listOf(
                        CourseBenefitResponse(
                            id = 1,
                            courseId = 1,
                            description = "Belajar Membuat Aplikasi Android untuk Pemula",
                        )
                    )
                ),
                CourseItemResponse(
                    id = 2,
                    name = "Belajar Membuat Aplikasi Android untuk Pemula Part 2",
                    imageUrl = "https://sinow.id/storage/course/1.png",
                    videoPreviewUrl = "https://sinow.id/storage/course/1.mp4",
                    level = "Pemula",
                    rating = 4.5,
                    categoryId = 2,
                    description = "Belajar Membuat Aplikasi Android untuk Pemula",
                    classCode = "ABC123",
                    totalModule = 10,
                    totalDuration = 100,
                    type = "Free",
                    price = 0,
                    promoDiscountPercentage = 0,
                    totalUser = 100,
                    courseBy = "Sinow",
                    createdBy = 2,
                    createdAt = "2021-08-10T09:00:00.000000Z",
                    updatedAt = "2021-08-10T09:00:00.000000Z",
                    category = CourseCategoryResponse(
                        id = 2,
                        name = "Teknologi",
                    ),
                    courseCreator = CourseCreatorResponse(
                        id = 2,
                        name = "Sinow",
                    ),
                    benefits = listOf(
                        CourseBenefitResponse(
                            id = 2,
                            courseId = 2,
                            description = "Belajar Membuat Aplikasi Android untuk Pemula",
                        )
                    )
                ),
            )
        )

        runTest {
            coEvery { dataSource.getCourses() } returns fakeCourseResponse
            repository.getCourses().map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 2)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { dataSource.getCourses() }
            }
        }
    }

    @Test
    fun `getCourse, with result error`() {
        runTest {
            coEvery { dataSource.getCourses() } throws IllegalStateException("Mock error")
            repository.getCourses().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getCourses() }
            }
        }
    }

    @Test
    fun `getCourse, with result empty`() {
        val fakeCourseResponse = CoursesResponse(
            status = "Failed",
            message = "Data course tidak ditemukan",
            data = null
        )

        runTest {
            coEvery { dataSource.getCourses() } returns fakeCourseResponse
            repository.getCourses().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getCourses() }
            }
        }
    }

    @Test
    fun `getCourse, with result loading`() {
        val mockResponse = mockk<CoursesResponse>()

        runTest {
            coEvery { dataSource.getCourses() } returns mockResponse
            repository.getCourses().map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getCourses() }
            }
        }
    }

    @Test
    fun `getCourseFilter, with result success`() {

        val fakeCourseResponse = CoursesResponse(
            status = "Success",
            message = "Berhasil mengambil data course",
            data = listOf(
                CourseItemResponse(
                    id = 1,
                    name = "Belajar Membuat Aplikasi Android untuk Pemula",
                    imageUrl = "https://sinow.id/storage/course/1.png",
                    videoPreviewUrl = "https://sinow.id/storage/course/1.mp4",
                    level = "Pemula",
                    rating = 4.5,
                    categoryId = 1,
                    description = "Belajar Membuat Aplikasi Android untuk Pemula",
                    classCode = "ABC123",
                    totalModule = 10,
                    totalDuration = 100,
                    type = "Free",
                    price = 0,
                    promoDiscountPercentage = 0,
                    totalUser = 100,
                    courseBy = "Sinow",
                    createdBy = 1,
                    createdAt = "2021-08-10T09:00:00.000000Z",
                    updatedAt = "2021-08-10T09:00:00.000000Z",
                    category = CourseCategoryResponse(
                        id = 1,
                        name = "Teknologi",
                    ),
                    courseCreator = CourseCreatorResponse(
                        id = 1,
                        name = "Sinow",
                    ),
                    benefits = listOf(
                        CourseBenefitResponse(
                            id = 1,
                            courseId = 1,
                            description = "Belajar Membuat Aplikasi Android untuk Pemula",
                        )
                    )
                ),
                CourseItemResponse(
                    id = 2,
                    name = "Belajar Membuat Aplikasi Android untuk Pemula Part 2",
                    imageUrl = "https://sinow.id/storage/course/1.png",
                    videoPreviewUrl = "https://sinow.id/storage/course/1.mp4",
                    level = "Pemula",
                    rating = 4.5,
                    categoryId = 2,
                    description = "Belajar Membuat Aplikasi Android untuk Pemula",
                    classCode = "ABC123",
                    totalModule = 10,
                    totalDuration = 100,
                    type = "Free",
                    price = 0,
                    promoDiscountPercentage = 0,
                    totalUser = 100,
                    courseBy = "Sinow",
                    createdBy = 2,
                    createdAt = "2021-08-10T09:00:00.000000Z",
                    updatedAt = "2021-08-10T09:00:00.000000Z",
                    category = CourseCategoryResponse(
                        id = 2,
                        name = "Teknologi",
                    ),
                    courseCreator = CourseCreatorResponse(
                        id = 2,
                        name = "Sinow",
                    ),
                    benefits = listOf(
                        CourseBenefitResponse(
                            id = 2,
                            courseId = 2,
                            description = "Belajar Membuat Aplikasi Android untuk Pemula",
                        )
                    )
                ),
            )
        )

        runTest {
            coEvery { dataSource.getCoursesFilter() } returns fakeCourseResponse
            repository.getCoursesFilter().map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 2)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { dataSource.getCoursesFilter() }
            }
        }
    }

    @Test
    fun `getCourseFilter, with result error`() {
        runTest {
            coEvery { dataSource.getCoursesFilter() } throws IllegalStateException("Mock error")
            repository.getCoursesFilter().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getCoursesFilter() }
            }
        }
    }

    @Test
    fun `getCourseFilter, with result empty`() {
        val fakeCourseResponse = CoursesResponse(
            status = "Failed",
            message = "Data course tidak ditemukan",
            data = emptyList()
        )

        runTest {
            coEvery { dataSource.getCoursesFilter() } returns fakeCourseResponse
            repository.getCoursesFilter().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getCoursesFilter() }
            }
        }
    }

    @Test
    fun `getCourseFilter, with result loading`() {
        val mockResponse = mockk<CoursesResponse>()

        runTest {
            coEvery { dataSource.getCoursesFilter() } returns mockResponse
            repository.getCoursesFilter().map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getCoursesFilter() }
            }
        }
    }

    @Test
    fun `getDetailCourse, with result loading`() {
        val mockResponse = mockk<DataResponse>()

        runTest {
            coEvery { dataSource.getDetailCourse(1) } returns mockResponse
            repository.getDetailCourse(1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getDetailCourse(1) }
            }
        }
    }

    @Test
    fun `getDetailCourse, with result error`() {
        runTest {
            coEvery { dataSource.getDetailCourse(1) } throws IllegalStateException("Mock error")
            repository.getDetailCourse(1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getDetailCourse(1) }
            }
        }
    }

    @Test
    fun `getUserModuleData, with result success`() {
        val fakeDataResponse = UserModuleDataResponse(
            status = "Success",
            message = "Berhasil mengambil data user module",
            data = com.group2.sinow.data.network.api.model.usermodule.DataResponse(
                ModuleResponse(
                    id = 1,
                    no = 1,
                    name = "Pengenalan",
                    videoUrl = "https://sinow.id/storage/module/1.mp4"
                )
            )
        )

        runTest {
            coEvery { dataSource.getUserModuleData(1, 1) } returns fakeDataResponse
            repository.getUserModuleData(1, 1).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.id, 1)
                coVerify { dataSource.getUserModuleData(1, 1) }
            }
        }
    }

    @Test
    fun `getUserModuleData, with result error`() {
        runTest {
            coEvery {
                dataSource.getUserModuleData(
                    1,
                    1
                )
            } throws IllegalStateException("Mock error")
            repository.getUserModuleData(1, 1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getUserModuleData(1, 1) }
            }
        }
    }

    @Test
    fun `getUserModuleData, with result loading`() {
        val mockResponse = mockk<UserModuleDataResponse>()

        runTest {
            coEvery { dataSource.getUserModuleData(1, 1) } returns mockResponse
            repository.getUserModuleData(1, 1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getUserModuleData(1, 1) }
            }
        }
    }

    @Test
    fun `getUserModuleData, with result empty`() {
        val fakeDataResponse = UserModuleDataResponse(
            status = "Failed",
            message = "Data user module tidak ditemukan",
            data = null
        )

        runTest {
            coEvery { dataSource.getUserModuleData(1, 1) } returns fakeDataResponse
            repository.getUserModuleData(1, 1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertEquals(data.payload, null)
                coVerify { dataSource.getUserModuleData(1, 1) }
            }
        }
    }

    @Test
    fun `followCourse, with result success`() {
        val fakeDataResponse = FollowCourseResponse(
            status = "Success",
            message = "Berhasil mengambil data follow course",
        )

        runTest {
            coEvery { dataSource.followCourse(1) } returns fakeDataResponse
            repository.followCourse(1).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.status, "Success")
                coVerify { dataSource.followCourse(1) }
            }
        }
    }

    @Test
    fun `followCourse, with result error`() {
        runTest {
            coEvery { dataSource.followCourse(1) } throws IllegalStateException("Mock error")
            repository.followCourse(1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.followCourse(1) }
            }
        }
    }

    @Test
    fun `followCourse, with result loading`() {
        val mockResponse = mockk<FollowCourseResponse>()

        runTest {
            coEvery { dataSource.followCourse(1) } returns mockResponse
            repository.followCourse(1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.followCourse(1) }
            }
        }
    }

    @Test
    fun `followCourse, with result empty`() {
        val fakeDataResponse = FollowCourseResponse(
            status = "Failed",
            message = "Data follow course tidak ditemukan",
        )

        runTest {
            coEvery { dataSource.followCourse(1) } returns fakeDataResponse
            repository.followCourse(1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertEquals(data.payload?.status, "Failed")
                coVerify { dataSource.followCourse(1) }
            }
        }
    }

    @Test
    fun `buyPremiumCourse, with result success`() {
        val fakeDataResponse = TransactionResponse(
            status = "Success",
            message = "Berhasil mengambil data transaction",
            data = TransactionDataResponse(
                token = "token",
                redirectUrl = "redirectUrl",
                transactionDetail = null
            )
        )

        runTest {
            coEvery {
                dataSource.buyPremiumCourse(
                    TransactionRequest(
                        courseId = 1,
                    )
                )
            } returns fakeDataResponse
            repository.buyPremiumCourse(1).map {
                delay(100)
                it
            }.test {
                delay(3000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.token, "token")
                coVerify {
                    dataSource.buyPremiumCourse(
                        TransactionRequest(
                            1
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `buyPremiumCourse, with result loading`() {
        val mockResponse = mockk<TransactionResponse>()

        runTest {
            coEvery {
                dataSource.buyPremiumCourse(
                    TransactionRequest(
                        courseId = 1,
                    )
                )
            } returns mockResponse
            repository.buyPremiumCourse(1).map {
                delay(100)
                it
            }.test {
                delay(100)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify {
                    dataSource.buyPremiumCourse(
                        TransactionRequest(
                            1
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `buyPremiumCourse, with result error`() {
        runTest {
            coEvery {
                dataSource.buyPremiumCourse(
                    TransactionRequest(
                        courseId = 1,
                    )
                )
            } throws IllegalStateException("Mock error")
            repository.buyPremiumCourse(1).map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify {
                    dataSource.buyPremiumCourse(
                        TransactionRequest(
                            1
                        )
                    )
                }
            }
        }
    }
}