package com.group2.sinow.model.course

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseCategory(
    val id: Int?,
    val name: String?
) : Parcelable
