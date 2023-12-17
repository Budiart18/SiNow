package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserCourseResponse(
    @SerializedName("userCourse")
    val userCourse: CourseDetailResponse?
)
