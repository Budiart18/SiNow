package com.group2.sinow.model.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: Int?,
    val type: String?,
    val title: String?,
    val content: String?,
    val userId: Int?,
    val isRead: Boolean?,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable
