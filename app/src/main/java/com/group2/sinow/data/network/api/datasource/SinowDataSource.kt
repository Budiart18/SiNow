package com.group2.sinow.data.network.api.datasource

import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.model.notification.NotificationDetailResponse
import com.group2.sinow.data.network.api.model.notification.NotificationResponse
import com.group2.sinow.data.network.api.service.SinowApiService

interface SinowDataSource {
    suspend fun getCategories(): CategoriesResponse
    suspend fun getCourses(
        search: String? = null,
        type: String? = null,
        category: Int? = null,
        level: String? = null,
        sortBy: String? = null
    ): CoursesResponse

    suspend fun getNotification(): NotificationResponse

    suspend fun getNotificationDetail(id: Int): NotificationDetailResponse

    //suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class SinowApiDataSource(private val service: SinowApiService) : SinowDataSource {

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun getCourses(
        search: String?,
        type: String?,
        category: Int?,
        level: String?,
        sortBy: String?
    ): CoursesResponse {
        return service.getCourses(search, type, category, level, sortBy)
    }

    override suspend fun getNotification(): NotificationResponse {
        return service.getNotifications()
    }

    override suspend fun getNotificationDetail(id: Int): NotificationDetailResponse {
        return service.getNotificationDetail(id)
    }

    /*    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
            return service.createOrder(orderRequest)
        }*/
}