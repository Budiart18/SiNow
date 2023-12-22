package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseData(
    val id: Int,
    val userId: Int?,
    val courseId: Int?,
    val isAccessible: Boolean?,
    val isFollowing: Boolean?,
    val lastSeen: String?,
    val progress: String?,
    val progressPercentage: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val course: CourseDetailData?
) : Parcelable