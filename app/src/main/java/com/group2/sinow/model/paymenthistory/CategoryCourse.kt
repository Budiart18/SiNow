package com.group2.sinow.model.paymenthistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryCourse(
    val id: Int?,
    val name: String?,
    val imageUrl: String?,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable
