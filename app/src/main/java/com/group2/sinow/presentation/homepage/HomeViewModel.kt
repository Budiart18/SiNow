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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CourseRepository) : ViewModel() {

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
            repository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }

    fun getCourses(category: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCourses(category).collect { result ->
                if (result is ResultWrapper.Success && result.payload != null) {
                    val sortedCourses = result.payload.sortedByDescending { it.rating }
                    _courses.postValue(ResultWrapper.Success(sortedCourses))
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