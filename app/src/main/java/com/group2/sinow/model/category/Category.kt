package com.group2.sinow.model.category

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int? = null,
    val categoryImage: String,
    val categoryName: String
) : Parcelable