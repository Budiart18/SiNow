package com.group2.sinow.data.network.api.model.updateprofile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DataResponse(
    @SerializedName("token")
    val token: String?
)
