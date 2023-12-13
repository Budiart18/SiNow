package com.group2.sinow.data.network.api.model.usermodule


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserModuleDataResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: DataResponse?
)