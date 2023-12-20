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
        const val ON_PROGRESS = "inProgress"
        const val PROGRESS_FINISH = "completed"
    }

    private val _courses = MutableLiveData<ResultWrapper<List<UserCourseData>>>()
    val courses: LiveData<ResultWrapper<List<UserCourseData>>>
        get() = _courses

    fun getUserCourses(search: String? = null, progress: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserCourses(
                search = search,
                progress = if(progress == "all") null else progress
            ).collect {
                _courses.postValue(it)
            }
        }
    }

    fun onTabClicked(search: String? = null, progress: String) {
        when (progress) {
            PROGRESS_ALL, ON_PROGRESS, PROGRESS_FINISH -> getUserCourses(search, progress)
        }
    }
}
