package com.group2.sinow.data.repository

import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.category.toCategoryList
import com.group2.sinow.data.network.api.model.course.toCourseList
import com.group2.sinow.model.category.Category
import com.group2.sinow.model.course.Course
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getCategories(): Flow<ResultWrapper<List<Category>>>

    fun getCourses(category: Int? = null): Flow<ResultWrapper<List<Course>>>

}

class CourseRepositoryImpl(
    private val dataSource: SinowDataSource
) : CourseRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            dataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getCourses(category: Int?): Flow<ResultWrapper<List<Course>>> {
        return proceedFlow {
            dataSource.getCourses(category).data?.toCourseList() ?: emptyList()
        }
    }

}