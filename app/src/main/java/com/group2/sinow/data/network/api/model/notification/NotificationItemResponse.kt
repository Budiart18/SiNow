package com.group2.sinow.data.network.api.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.notification.Notification

@Keep
data class NotificationItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("isRead")
    val isRead: Boolean?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)

fun NotificationItemResponse.toNotification() = Notification(
    id = this.id ?: 0,
    type = this.type.orEmpty(),
    title = this.title.orEmpty(),
    content = this.content.orEmpty(),
    userId = this.userId ?: 0,
    isRead = this.isRead ?: false,
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty()
)

fun Collection<NotificationItemResponse>.toNotificationList() = this.map {
    it.toNotification()
}
