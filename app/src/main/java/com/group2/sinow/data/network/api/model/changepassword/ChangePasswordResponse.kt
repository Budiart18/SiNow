package com.group2.sinow.data.network.api.model.changepassword


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ChangePasswordResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?
)