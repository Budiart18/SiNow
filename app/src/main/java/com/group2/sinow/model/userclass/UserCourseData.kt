package com.group2.sinow.model.userclass


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCourseData(
    val id: Int?,
    val userId: Int?,
    val courseId: Int?,
    val isAccessible: Boolean?,
    val lastSeen: String?,
    val progress: String?,
    val progressPercentage: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val course: UserCourses?
):Parcelable
