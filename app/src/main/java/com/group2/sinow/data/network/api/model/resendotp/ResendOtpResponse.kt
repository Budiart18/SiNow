package com.group2.sinow.data.network.api.model.resendotp

import com.google.gson.annotations.SerializedName

data class ResendOtpResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
