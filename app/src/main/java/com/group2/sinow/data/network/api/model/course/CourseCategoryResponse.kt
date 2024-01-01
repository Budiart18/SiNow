package com.group2.sinow.data.network.api.model.course

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.course.CourseCategory

@Keep
data class CourseCategoryResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)

fun CourseCategoryResponse.toCourseCategory() = CourseCategory(
    id = this.id ?: 0,
    name = this.name.orEmpty()
)

fun Collection<CourseCategoryResponse>.toCourseCategoryList() = this.map {
    it.toCourseCategory()
}
