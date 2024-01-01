package com.group2.sinow.presentation.detail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.network.api.model.followcourse.FollowCourseResponse
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.model.transaction.TransactionData
import com.group2.sinow.model.usermodule.ModuleData
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailCourseViewModel(
    private val extra: Bundle?,
    private val repository: CourseRepository
) : ViewModel() {

    val detail = extra?.getInt(DetailCourseActivity.EXTRA_COURSE)

    private val _detailCourse = MutableLiveData<ResultWrapper<CourseData?>>()
    val detailCourseData: LiveData<ResultWrapper<CourseData?>>
        get() = _detailCourse

    private val _userModule = MutableLiveData<ResultWrapper<ModuleData?>>()
    val userModule: LiveData<ResultWrapper<ModuleData?>>
        get() = _userModule

    private val _isFollowingCourse = MutableLiveData<ResultWrapper<FollowCourseResponse>>()
    val isFollowingCourse: LiveData<ResultWrapper<FollowCourseResponse>>
        get() = _isFollowingCourse

    private val _buyPremiumCourseResult = MutableLiveData<ResultWrapper<TransactionData>>()
    val buyPremiumCourseResult: LiveData<ResultWrapper<TransactionData>>
        get() = _buyPremiumCourseResult

    fun getDetailCourse(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDetailCourse(id).collect {
                _detailCourse.postValue(it)
            }
        }
    }

    fun getUserModule(courseId: Int?, userModuleId: Int?) {
        viewModelScope.launch {
            repository.getUserModuleData(courseId, userModuleId).collect {
                _userModule.postValue(it)
            }
        }
    }

    fun followCourse(courseId: Int?) {
        viewModelScope.launch {
            repository.followCourse(courseId).collect {
                _isFollowingCourse.postValue(it)
            }
        }
    }

    fun buyPremiumCourse(courseId: Int?) {
        viewModelScope.launch {
            repository.buyPremiumCourse(courseId).collect {
                _buyPremiumCourseResult.postValue(it)
            }
        }
    }
}
