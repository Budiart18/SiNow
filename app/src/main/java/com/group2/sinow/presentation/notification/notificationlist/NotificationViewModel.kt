package com.group2.sinow.presentation.notification.notificationlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.NotificationRepository
import com.group2.sinow.model.notification.Notification
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    private val _notifications = MutableLiveData<ResultWrapper<List<Notification>>>()
    val notifications: LiveData<ResultWrapper<List<Notification>>>
        get() = _notifications

    fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotification().collect{
                _notifications.postValue(it)
            }
        }
    }

}