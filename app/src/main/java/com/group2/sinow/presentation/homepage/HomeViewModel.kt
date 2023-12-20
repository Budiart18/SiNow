package com.group2.sinow.presentation.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.model.category.Category
import com.group2.sinow.model.course.Course
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CourseRepository) : ViewModel() {

    companion object {
        const val LIMIT_COURSE_SIZE = 6
        const val SORT_BY_POPULAR = "terpopuler"
    }

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    val categories: LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _popularCourseCategories = MutableLiveData<ResultWrapper<List<Category>>>()
    val popularCourseCategories: LiveData<ResultWrapper<List<Category>>>
        get() = _popularCourseCategories

    private val _selectedCategory = MutableLiveData<Category>()
    val selectedCategory: LiveData<Category>
        get() = _selectedCategory

    private val _courses = MutableLiveData<ResultWrapper<List<Course>>>()
    val courses: LiveData<ResultWrapper<List<Course>>>
        get() = _courses

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }

    fun getPopularCourseCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect { result ->
                if (result is ResultWrapper.Success && result.payload != null) {
                    val allCategory = Category(id = 0, categoryImage = "", categoryName = "All")
                    val newCategories = mutableListOf(allCategory)
                    newCategories.addAll(result.payload)
                    _popularCourseCategories.postValue(ResultWrapper.Success(newCategories))
                } else {
                    _popularCourseCategories.postValue(result)
                }
            }
        }
    }

    fun getCourses(categoryId: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCourses(
                category = if (categoryId == 0) null else categoryId,
                sortBy = SORT_BY_POPULAR
            ).collect { result ->
                if (result is ResultWrapper.Success && result.payload != null) {
                    val limitCourse = result.payload.take(LIMIT_COURSE_SIZE)
                    _courses.postValue(ResultWrapper.Success(limitCourse))
                } else {
                    _courses.postValue(result)
                }
            }
        }
    }


    fun changeSelectedCategory(newCategory: Category) {
        _selectedCategory.value = newCategory
    }

}