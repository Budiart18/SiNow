package com.group2.sinow.data.network.api.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeleteNotificationResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?
)
