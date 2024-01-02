package com.group2.sinow.presentation.allpopularcourse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.category.Category
import com.group2.sinow.tools.MainCoroutineRule
import com.group2.sinow.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AllPopularCourseViewModelTest {

    @MockK
    private lateinit var courseRepository: CourseRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(
        UnconfinedTestDispatcher()
    )
    private lateinit var allPopularCourseViewModel: AllPopularCourseViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        allPopularCourseViewModel = AllPopularCourseViewModel(courseRepository, userRepository)
    }

    @Test
    fun `getCategories gets called`() {
        coEvery { courseRepository.getCategories() } returns flowOf(ResultWrapper.Success(emptyList()))

        allPopularCourseViewModel.getCategories()

        coEvery { courseRepository.getCategories() }
    }

    @Test
    fun `getCourses gets called`() {
        coEvery { courseRepository.getCourses() } returns flowOf(ResultWrapper.Success(emptyList()))

        allPopularCourseViewModel.getCourses()

        coEvery { courseRepository.getCourses() }
    }

    @Test
    fun `changeSelectedCategory gets called`() {
        val category = Category(1, "image", "name")

        allPopularCourseViewModel.changeSelectedCategory(category)

        assertEquals(category, allPopularCourseViewModel.selectedCategory.value)
    }
}
