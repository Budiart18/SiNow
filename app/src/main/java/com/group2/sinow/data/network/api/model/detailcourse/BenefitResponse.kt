package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.BenefitData

@Keep
data class BenefitResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("description")
    val description: String?
)

fun BenefitResponse.toBenefitData() = BenefitData(
    id = this.id ?: 0,
    description = this.description.orEmpty()
)