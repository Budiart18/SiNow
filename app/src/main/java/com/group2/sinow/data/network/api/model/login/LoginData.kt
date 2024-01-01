package com.group2.sinow.data.network.api.model.login

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginData(
    @SerializedName("token")
    val token: String?
)
