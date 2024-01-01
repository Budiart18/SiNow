package com.group2.sinow.data.network.api.model.course

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.course.CourseCreator

@Keep
data class CourseCreatorResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)

fun CourseCreatorResponse.toCourseCreator() = CourseCreator(
    id = this.id ?: 0,
    name = this.name.orEmpty()
)

fun Collection<CourseCreatorResponse>.toCourseCreatorList() = this.map {
    it.toCourseCreator()
}
