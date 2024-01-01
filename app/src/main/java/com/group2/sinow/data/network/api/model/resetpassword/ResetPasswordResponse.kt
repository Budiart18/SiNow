package com.group2.sinow.data.network.api.model.resetpassword

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(

    @field:SerializedName("email")
    val email: String? = null
)
