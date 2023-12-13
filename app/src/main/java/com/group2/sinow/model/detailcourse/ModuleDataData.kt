package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleDataData(
    val id: Int?,
    val no: Int?,
    val name: String?
) : Parcelable
