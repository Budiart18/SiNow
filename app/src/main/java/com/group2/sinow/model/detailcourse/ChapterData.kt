package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChapterData(
    val id: Int?,
    val no: Int?,
    val name: String?,
    val totalDuration: Int?,
    val userModules: List<UserModuleData>?
) : Parcelable
