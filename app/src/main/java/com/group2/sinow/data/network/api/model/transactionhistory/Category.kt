package com.group2.sinow.data.network.api.model.transactionhistory

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.paymenthistory.CategoryCourse

@Keep
data class Category(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)
fun Category.toItemCategory() = CategoryCourse(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    imageUrl = this.imageUrl.orEmpty(),
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty()
)
