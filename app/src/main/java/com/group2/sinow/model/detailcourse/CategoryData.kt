package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryData(
    val id: Int?,
    val name: String?
) : Parcelable
