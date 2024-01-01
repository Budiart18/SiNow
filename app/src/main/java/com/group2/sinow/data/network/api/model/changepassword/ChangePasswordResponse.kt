package com.group2.sinow.data.network.api.model.changepassword

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChangePasswordResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?
)
