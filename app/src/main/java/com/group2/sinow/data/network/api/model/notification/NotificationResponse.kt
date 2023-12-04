package com.group2.sinow.data.network.api.model.notification


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class NotificationResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("data")
    val data: List<NotificationItemResponse>?
)