package com.group2.sinow.presentation.course

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

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()

    val categories: LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _courses = MutableLiveData<ResultWrapper<List<Course>>>()

    val courses: LiveData<ResultWrapper<List<Course>>>
        get() = _courses

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _selectedType = MutableLiveData<String>()
    val selectedType: LiveData<String>
        get() = _selectedType

    private val _selectedCategories = MutableLiveData<List<Category>>()
    val selectedCategories: LiveData<List<Category>?>
        get() = _selectedCategories

    fun resetFilter() {
        _selectedCategories.postValue(listOf())
        _selectedType.postValue("")
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }

    fun getCourses(
        search: String? = null,
        type: String? = null,
        category: List<Int>? = null,
        level: List<String>? = null,
        sortBy: String? = null
    ) {
        viewModelScope.launch {
            repository.getCourses(
                search = search,
                type = if (type == "all") null else type,
                category = category,
                level = level,
                sortBy = sortBy
            ).collect {
                _courses.postValue(it)
            }
        }
    }

    fun addSelectedCategory(category: Category) {
        val updatedList = _selectedCategories.value.orEmpty().toMutableList()
        updatedList.add(category)
        _selectedCategories.value = updatedList.distinct()
    }

    fun removeSelectedCategory(category: Category) {
        val updatedList = _selectedCategories.value.orEmpty().toMutableList()
        updatedList.remove(category)
        _selectedCategories.value = updatedList.distinct()
    }

    fun clearSelectedCategories() {
        _selectedCategories.value = emptyList()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedType(type: String) {
        _selectedType.value = type
    }
}
