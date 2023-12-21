package com.group2.sinow.presentation.allpopularcourse

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

class AllPopularCourseViewModel(private val repository: CourseRepository) : ViewModel() {

    companion object {
        const val SORT_BY_POPULAR = "terpopuler"
    }

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    val categories: LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _selectedCategory = MutableLiveData<Category>()
    val selectedCategory: LiveData<Category>
        get() = _selectedCategory

    private val _courses = MutableLiveData<ResultWrapper<List<Course>>>()
    val courses: LiveData<ResultWrapper<List<Course>>>
        get() = _courses

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect { result ->
                if (result is ResultWrapper.Success && result.payload != null) {
                    val allCategory = Category(id = 0, categoryImage = "", categoryName = "All")
                    val newCategories = mutableListOf(allCategory)
                    newCategories.addAll(result.payload)
                    _categories.postValue(ResultWrapper.Success(newCategories))
                } else {
                    _categories.postValue(result)
                }
            }
        }
    }

    fun getCourses(category: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCourses(
                category = if (category == 0) null else category,
                sortBy = SORT_BY_POPULAR
            ).collect { result ->
                _courses.postValue(result)
            }
        }
    }


    fun changeSelectedCategory(newCategory: Category) {
        _selectedCategory.value = newCategory
    }

}