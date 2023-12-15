package com.group2.sinow.data.network.api.model.changepassword

data class ChangePasswordRequest(
    val oldPassword: String?,
    val newPassword: String?,
    val confirmNewPassword: String?
)
