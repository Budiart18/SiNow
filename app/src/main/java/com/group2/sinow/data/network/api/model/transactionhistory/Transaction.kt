package com.group2.sinow.data.network.api.model.transactionhistory

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.paymenthistory.TransactionUser

@Keep
data class Transaction(
    @SerializedName("id")
    val id: String?,
    @SerializedName("noOrder")
    val noOrder: String?,
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
    @SerializedName("paymentUrl")
    val paymentUrl: String?,
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("paidAt")
    val paidAt: String?,
    @SerializedName("expiredAt")
    val expiredAt: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("Course")
    val course: Course?
)

fun Transaction.toItemTransaction() = TransactionUser(
    id = this.id.orEmpty(),
    noOrder = this.noOrder.orEmpty(),
    userId = this.userId ?: 0,
    courseId = this.courseId ?: 0,
    coursePrice = this.coursePrice ?: 0,
    discountPrice = this.discountPrice ?: 0,
    taxPrice = this.taxPrice ?: 0,
    totalPrice = this.totalPrice ?: 0,
    promoDiscountPercentage = this.promoDiscountPercentage ?: 0,
    taxPercentage = this.taxPercentage ?: 0,
    paymentUrl = this.paymentUrl.orEmpty(),
    paymentMethod = this.paymentMethod.orEmpty(),
    status = this.status.orEmpty(),
    paidAt = this.paidAt.orEmpty(),
    expiredAt = this.expiredAt.orEmpty(),
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty(),
    courseUser = this.course?.toItemCourse()
)

fun Collection<Transaction>.toTransactionList() = this.map {
    it.toItemTransaction()
}
