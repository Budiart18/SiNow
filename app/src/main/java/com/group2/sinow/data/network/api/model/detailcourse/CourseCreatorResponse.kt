package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.CourseCreatorData

@Keep
data class CourseCreatorResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)
fun CourseCreatorResponse.toCourseCreatorData() = CourseCreatorData(
    id = this.id ?: 0,
    name = this.name.orEmpty()
)