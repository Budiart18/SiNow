package com.group2.sinow.data.network.api.model.userclass

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ClassesResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<Data>?
)
