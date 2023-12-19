package com.group2.sinow.model.profile

data class ProfileAuth(
    val id: Int?,
    val email: String?,
    val phoneNumber: String?,
    val password: String?,
    val userId: Int?,
    val isEmailVerified: Boolean?,
    val createdAt: String?,
    val updatedAt: String?
)
