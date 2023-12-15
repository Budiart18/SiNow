package com.group2.sinow.data.network.api.model.profile


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.group2.sinow.model.profile.ProfileAuth

@Keep
data class ProfileAuthResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("userId")
    val userId: Int?
)

fun ProfileAuthResponse.toProfileAuth() = ProfileAuth(
    id = this.id ?: 0,
    email = this.email.orEmpty(),
    phoneNumber = this.phoneNumber.orEmpty(),
    userId = this.userId ?: 0
)