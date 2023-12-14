package com.group2.sinow.data.network.api.model.profile


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ProfileResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: ProfileDataResponse
)