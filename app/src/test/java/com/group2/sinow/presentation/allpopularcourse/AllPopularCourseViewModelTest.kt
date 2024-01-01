package com.group2.sinow.presentation.allpopularcourse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.model.category.Category
import com.group2.sinow.utils.ResultWrapper
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllPopularCourseViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val repository = mockk<CourseRepository>()
    private lateinit var allPopularCourseViewModel: AllPopularCourseViewModel

    @Before
    fun setUp() {
        allPopularCourseViewModel = AllPopularCourseViewModel(repository)
    }

    @Test
    fun `getCategories gets called`() {
        coEvery { repository.getCategories() } returns flowOf(ResultWrapper.Success(emptyList()))

        allPopularCourseViewModel.getCategories()

        coEvery { repository.getCategories() }
    }

    @Test
    fun `getCourses gets called`() {
        coEvery { repository.getCourses() } returns flowOf(ResultWrapper.Success(emptyList()))

        allPopularCourseViewModel.getCourses()

        coEvery { repository.getCourses() }
    }

    @Test
    fun `changeSelectedCategory gets called`() {
        val category = Category(1, "image", "name")

        allPopularCourseViewModel.changeSelectedCategory(category)

        assertEquals(category, allPopularCourseViewModel.selectedCategory.value)
    }
}
