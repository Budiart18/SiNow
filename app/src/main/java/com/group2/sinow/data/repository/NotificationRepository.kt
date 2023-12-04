package com.group2.sinow.data.repository

import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.notification.toNotification
import com.group2.sinow.data.network.api.model.notification.toNotificationList
import com.group2.sinow.model.notification.Notification
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotification(): Flow<ResultWrapper<List<Notification>>>

    fun getNotificationDetail(id: Int): Flow<ResultWrapper<Notification>>

}

class NotificationRepositoryImpl(
    private val dataSource: SinowDataSource
) : NotificationRepository {

    override fun getNotification(): Flow<ResultWrapper<List<Notification>>> {
        return proceedFlow {
            dataSource.getNotification().data?.toNotificationList() ?: emptyList()
        }
    }

    override fun getNotificationDetail(id: Int): Flow<ResultWrapper<Notification>> {
        return proceedFlow {
            dataSource.getNotificationDetail(id).data.toNotification()
        }
    }

}