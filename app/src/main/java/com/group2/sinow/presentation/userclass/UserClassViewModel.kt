package com.group2.sinow.presentation.userclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.userclass.UserCourseData
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserClassViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        const val PROGRESS_ALL = "all"
    }

    private val _courses = MutableLiveData<ResultWrapper<List<UserCourseData>>>()
    val courses: LiveData<ResultWrapper<List<UserCourseData>>>
        get() = _courses

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _selectedProgress = MutableLiveData<String>()
    val selectedProgress: LiveData<String>
        get() = _selectedProgress

    fun getUserCourses(search: String? = null, progress: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserCourses(
                search = search,
                progress = if(progress == PROGRESS_ALL) null else progress
            ).collect {
                _courses.postValue(it)
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedProgress(progress: String) {
        _selectedProgress.value = progress
    }
}
