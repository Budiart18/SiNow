package com.group2.sinow.data.network.api.model.profile


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.group2.sinow.model.profile.ProfileData

@Keep
data class ProfileDataResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("photoProfileUrl")
    val photoProfileUrl: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("Auth")
    val auth: ProfileAuthResponse?
)

fun ProfileDataResponse.toProfileData() = ProfileData(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    photoProfileUrl = this.photoProfileUrl.orEmpty(),
    country = this.country.orEmpty(),
    city = this.city.orEmpty(),
    role = this.role.orEmpty(),
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty(),
    auth = this.auth?.toProfileAuth()
)