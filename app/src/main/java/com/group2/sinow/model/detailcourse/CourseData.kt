package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseData(
    val id: Int,
    val userId: Int?,
    val courseId: Int?,
    val isAccessible: Boolean?,
    val lastSeen: String?,
    val progress: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val course: CourseDetailData?
) : Parcelable