package com.group2.sinow.data.network.api.model.userclass

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.userclass.CategoryCourse

@Keep
data class Category(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)

fun Category.toUserCourseCategory() = CategoryCourse(
    id = this.id,
    name = this.name
)
