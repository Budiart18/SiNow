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
            data = null
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

}