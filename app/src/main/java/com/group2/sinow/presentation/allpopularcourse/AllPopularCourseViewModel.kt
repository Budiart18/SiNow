package com.group2.sinow.presentation.allpopularcourse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.category.Category
import com.group2.sinow.model.course.Course
import com.group2.sinow.model.profile.ProfileData
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllPopularCourseViewModel(
    private val repository: CourseRepository,
    private val userRepository: UserRepository
) : ViewModel() {

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

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _userData = MutableLiveData<ResultWrapper<ProfileData>>()
    val userData: LiveData<ResultWrapper<ProfileData>>
        get() = _userData

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserData().collect {
                _userData.postValue(it)
            }
        }
    }

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

    fun getCourses(search: String? = null, category: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCourses(
                search = search,
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

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
