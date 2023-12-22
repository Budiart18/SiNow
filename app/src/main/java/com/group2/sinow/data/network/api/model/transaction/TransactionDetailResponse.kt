package com.group2.sinow.data.network.api.model.transaction


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.group2.sinow.model.transaction.TransactionDetail

@Keep
data class TransactionDetailResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("courseId")
    val courseId: Int?,
    @SerializedName("coursePrice")
    val coursePrice: Int?,
    @SerializedName("discountPrice")
    val discountPrice: Int?,
    @SerializedName("taxPrice")
    val taxPrice: Int?,
    @SerializedName("totalPrice")
    val totalPrice: Int?,
    @SerializedName("promoDiscountPercentage")
    val promoDiscountPercentage: Int?,
    @SerializedName("taxPercentage")
    val taxPercentage: Int?,
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("paidAt")
    val paidAt: String?,
    @SerializedName("expiredAt")
    val expiredAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("paymentUrl")
    val paymentUrl: String?
)

fun TransactionDetailResponse.toTransactionDetail() = TransactionDetail(
    id = this.id.orEmpty(),
    userId = this.userId ?: 0,
    courseId = this.courseId ?: 0,
    coursePrice = this.coursePrice ?: 0,
    discountPrice = this.discountPrice ?: 0,
    taxPrice = this.taxPrice ?: 0,
    totalPrice = this.totalPrice ?: 0,
    promoDiscountPercentage = this.promoDiscountPercentage ?: 0,
    taxPercentage = this.taxPercentage ?: 0,
    paymentMethod = this.paymentMethod.orEmpty(),
    status = this.status.orEmpty(),
    paidAt = this.paidAt.orEmpty(),
    expiredAt = this.expiredAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty(),
    createdAt = this.createdAt.orEmpty(),
    paymentUrl = this.paymentUrl.orEmpty()
)