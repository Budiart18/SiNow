package com.group2.sinow.data.network.api.model.userclass

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.userclass.UserCourseData

@Keep
data class Data(
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
    val progress: String?,
    @SerializedName("progressPercentage")
    val progressPercentage: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("Course")
    val course: Course?
)

fun Data.toUserCourseData() = UserCourseData(
    id = this.id ?: 0,
    userId = this.userId ?: 0,
    courseId = this.courseId ?: 0,
    isAccessible = this.isAccessible,
    lastSeen = this.lastSeen.orEmpty(),
    progress = this.progress.orEmpty(),
    progressPercentage = this.progressPercentage ?: 0,
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty(),
    course = this.course?.toUserCourses()
)

fun Collection<Data>.toUserListCourseData() = this.map {
    it.toUserCourseData()
}
