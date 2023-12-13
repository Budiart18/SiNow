package com.group2.sinow.data.repository

import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.model.category.toCategoryList
import com.group2.sinow.data.network.api.model.course.toCourseList
import com.group2.sinow.data.network.api.model.detailcourse.toCourseDetail
import com.group2.sinow.data.network.api.model.usermodule.toModuleData
import com.group2.sinow.model.category.Category
import com.group2.sinow.model.course.Course
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.model.usermodule.ModuleData
import com.group2.sinow.utils.ResultWrapper
import com.group2.sinow.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getCategories(): Flow<ResultWrapper<List<Category>>>

    fun getCourses(
        search: String? = null,
        type: String? = null,
        category: Int? = null,
        level: String? = null,
        sortBy: String? = null
    ): Flow<ResultWrapper<List<Course>>>

    fun getDetailCourse(id: Int): Flow<ResultWrapper<CourseData?>>

    fun getUserModuleData(courseId: Int?, userModuleId: Int?): Flow<ResultWrapper<ModuleData?>>

}

class CourseRepositoryImpl(
    private val dataSource: SinowDataSource
) : CourseRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            dataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getCourses(
        search: String?,
        type: String?,
        category: Int?,
        level: String?,
        sortBy: String?
    ): Flow<ResultWrapper<List<Course>>> {
        return proceedFlow {
            dataSource.getCourses(search, type, category, level, sortBy).data?.toCourseList() ?: emptyList()
        }
    }

    override fun getDetailCourse(id: Int): Flow<ResultWrapper<CourseData?>> {
        return proceedFlow {
            dataSource.getDetailCourse(id).data?.toCourseDetail()
        }
    }

    override fun getUserModuleData(
        courseId: Int?,
        userModuleId: Int?
    ): Flow<ResultWrapper<ModuleData?>> {
        return proceedFlow {
            dataSource.getUserModuleData(courseId, userModuleId).data?.module?.toModuleData()
        }
    }

}