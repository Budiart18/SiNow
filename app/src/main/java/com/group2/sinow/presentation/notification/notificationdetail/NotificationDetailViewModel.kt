package com.group2.sinow.presentation.notification.notificationdetail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.NotificationRepository
import com.group2.sinow.model.notification.Notification
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationDetailViewModel(
    private val extra: Bundle?,
    private val repository: NotificationRepository
) : ViewModel() {

    val notification = extra?.getParcelable<Notification>(NotificationDetailActivity.EXTRA_NOTIFICATION)

    private val _notification = MutableLiveData<ResultWrapper<Notification>>()
    val notificationData: LiveData<ResultWrapper<Notification>>
        get() = _notification

    private val _deleteNotificationResult = MutableLiveData<ResultWrapper<Boolean>>()
    val deleteNotificationResult: LiveData<ResultWrapper<Boolean>>
        get() = _deleteNotificationResult

    fun getNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotificationDetail(notification?.id ?: 0).collect{
                _notification.postValue(it)
            }
        }
    }

    fun deleteNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNotification(notification?.id ?: 0).collect{
                _deleteNotificationResult.postValue(it)
            }
        }
    }

}