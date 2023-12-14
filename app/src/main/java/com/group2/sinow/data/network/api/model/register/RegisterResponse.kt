package com.group2.sinow.data.network.api.model.register

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
