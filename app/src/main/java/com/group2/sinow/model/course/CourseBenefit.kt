package com.group2.sinow.model.course

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseBenefit(
    val id: Int?,
    val courseId: Int?,
    val description: String?
) : Parcelable
