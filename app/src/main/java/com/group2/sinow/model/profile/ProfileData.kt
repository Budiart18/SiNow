package com.group2.sinow.model.profile

data class ProfileData(
    val id: Int?,
    val name: String?,
    val photoProfileUrl: String?,
    val country: String?,
    val city: String?,
    val role: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val auth: ProfileAuth?
)