package com.group2.sinow.data.network.api.model.course


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
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