package com.group2.sinow.data.network.api.model.updateprofile

import okhttp3.MultipartBody

data class UpdateUserDataRequest(
    val name: String?,
    val email: String?,
    val phoneNumber: String?,
    val country: String?,
    val city: String?,
    val image: MultipartBody.Part? = null
)