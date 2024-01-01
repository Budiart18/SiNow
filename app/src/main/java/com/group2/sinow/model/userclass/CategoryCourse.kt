package com.group2.sinow.model.userclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryCourse(
    val id: Int?,
    val name: String?
) : Parcelable
