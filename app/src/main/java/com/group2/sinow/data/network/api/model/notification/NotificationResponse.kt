package com.group2.sinow.data.network.api.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("data")
    val data: List<NotificationItemResponse>?
)
