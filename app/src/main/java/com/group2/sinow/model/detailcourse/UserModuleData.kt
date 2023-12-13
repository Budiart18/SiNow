package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModuleData(
    val id: Int?,
    val status: String?,
    val moduleData: ModuleDataData?
) : Parcelable
