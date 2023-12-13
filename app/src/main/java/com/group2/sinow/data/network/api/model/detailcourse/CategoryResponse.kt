package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.CategoryData

@Keep
data class CategoryResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)

fun CategoryResponse.toCategoryData() = CategoryData(
    id = this.id ?: 0,
    name = this.name.orEmpty()
)