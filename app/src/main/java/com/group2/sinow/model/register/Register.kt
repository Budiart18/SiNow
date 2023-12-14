package com.group2.sinow.model.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Register(
    val message: String? = null,
    val status: String? = null

):Parcelable
