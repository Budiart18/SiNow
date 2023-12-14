package com.group2.sinow.data.network.api.model.login


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class LoginData(
    @SerializedName("token")
    val token: String?
)