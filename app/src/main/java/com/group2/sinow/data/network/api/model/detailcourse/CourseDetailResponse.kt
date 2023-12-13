package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.CourseData

@Keep
data class CourseDetailResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("courseId")
    val courseId: Int?,
    @SerializedName("isAccessible")
    val isAccessible: Boolean?,
    @SerializedName("lastSeen")
    val lastSeen: String?,
    @SerializedName("progress")
    val progress: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("Course")
    val course: CourseResponse?
)

fun CourseDetailResponse.toCourseDetail() = CourseData(
    id = this.id ?: 0,
    userId = this.userId ?: 0,
    courseId = this.courseId ?: 0,
    isAccessible = this.isAccessible,
    lastSeen = this.lastSeen.orEmpty(),
    progress = this.progress ?: 0,
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty(),
    course = this.course?.toCourseDetailData()
)