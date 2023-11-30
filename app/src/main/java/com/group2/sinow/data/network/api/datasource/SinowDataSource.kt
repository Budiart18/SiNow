package com.group2.sinow.data.network.api.datasource

import com.group2.sinow.data.network.api.model.category.CategoriesResponse
import com.group2.sinow.data.network.api.model.course.CoursesResponse
import com.group2.sinow.data.network.api.service.SinowApiService

interface SinowDataSource {
    suspend fun getCategories(): CategoriesResponse
    suspend fun getCourses(category: Int? = null): CoursesResponse
    //suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class SinowApiDataSource(private val service: SinowApiService) : SinowDataSource {

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun getCourses(category: Int?): CoursesResponse {
        return service.getCourses(category)
    }

/*    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }*/
}