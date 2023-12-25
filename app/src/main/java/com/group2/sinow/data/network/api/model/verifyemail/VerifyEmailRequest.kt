package com.group2.sinow.data.network.api.model.verifyemail

import androidx.annotation.Keep

@Keep
data class VerifyEmailRequest(
    val email: String,
    val otpCode: String
)