package com.group2.sinow.presentation.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.model.category.Category
import com.group2.sinow.model.course.Course
import com.group2.sinow.model.filter.Filter
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseViewModel (private val repository: CourseRepository) : ViewModel() {

    companion object {
        const val TYPE_ALL = "all"
        const val TYPE_PREMIUM = "premium"
        const val TYPE_FREE = "gratis"
    }
    private val selectedCategories = mutableListOf<Category>()

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    val categories: LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _courses = MutableLiveData<ResultWrapper<List<Course>>>()
    val courses: LiveData<ResultWrapper<List<Course>>>
        get() = _courses

    private val _appliedFilters = MutableLiveData<Filter>()
    val appliedFilters: LiveData<Filter>
        get() = _appliedFilters

    fun getFilterCourses(search: String? = null, type: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCourses(
                search = search,
                type = if(type == "all") null else type
            ).collect{
                _courses.postValue(it)
            }
        }
    }

    fun applyFilters(filter: Filter) {
        viewModelScope.launch {
            repository.getCobaFilter(filter).collect {
            }
            _appliedFilters.value = filter
        }
    }

    fun onCategoryChecked(category: Category){
        selectedCategories.add(category)
    }
    fun onCategoryUncheck(category: Category){
        selectedCategories.remove(category)
    }

    fun getSelectedCategory() = selectedCategories.map { it.id }

    fun onTabClicked(search: String? = null, type: String) {
        when (type) {
            TYPE_ALL, TYPE_PREMIUM, TYPE_FREE -> getFilterCourses(search, type)
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }

}