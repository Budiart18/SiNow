package com.group2.sinow.data.network.api.model.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)
