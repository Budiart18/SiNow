package com.group2.sinow.data.network.api.model.category


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.group2.sinow.model.category.Category

@Keep
data class CategoryItemResponse(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)

fun CategoryItemResponse.toCategory() = Category(
    id = this.id ?: 0,
    categoryName = this.name.orEmpty(),
    categoryImage = this.imageUrl.orEmpty()
)

fun Collection<CategoryItemResponse>.toCategoryList() = this.map {
    it.toCategory()
}