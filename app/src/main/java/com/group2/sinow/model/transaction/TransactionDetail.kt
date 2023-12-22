package com.group2.sinow.model.transaction

data class TransactionDetail(
    val id: String?,
    val userId: Int?,
    val courseId: Int?,
    val coursePrice: Int?,
    val discountPrice: Int?,
    val taxPrice: Int?,
    val totalPrice: Int?,
    val promoDiscountPercentage: Int?,
    val taxPercentage: Int?,
    val paymentMethod: String?,
    val status: String?,
    val paidAt: String?,
    val expiredAt: String?,
    val updatedAt: String?,
    val createdAt: String?,
    val paymentUrl: String?
)
