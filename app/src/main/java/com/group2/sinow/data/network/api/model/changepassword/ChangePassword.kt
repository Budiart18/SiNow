package com.group2.sinow.data.network.api.model.changepassword

import com.google.errorprone.annotations.Keep

@Keep
data class ChangePassword(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)