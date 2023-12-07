package com.group2.sinow.data.network.api.model.notification


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class DeleteNotificationResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?
)