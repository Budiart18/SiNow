package com.group2.sinow.data.network.api.model.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.profile.ProfileAuth

@Keep
data class ProfileAuthResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("isEmailVerified")
    val isEmailVerified: Boolean?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)

fun ProfileAuthResponse.toProfileAuth() = ProfileAuth(
    id = this.id ?: 0,
    email = this.email.orEmpty(),
    phoneNumber = this.phoneNumber.orEmpty(),
    password = this.password.orEmpty(),
    userId = this.userId ?: 0,
    isEmailVerified = this.isEmailVerified ?: false,
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty()
)
