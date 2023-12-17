package com.group2.sinow.model.detailcourse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BenefitData(
    val id: Int?,
    val description: String?
) : Parcelable