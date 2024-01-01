package com.group2.sinow.data.network.api.model.followcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FollowCourseResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?
)
