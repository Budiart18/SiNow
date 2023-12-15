package com.group2.sinow.data.network.api.model.updateprofile


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class DataResponse(
    @SerializedName("token")
    val token: String?
)