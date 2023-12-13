package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.UserModuleData

@Keep
data class UserModuleResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("moduleData")
    val moduleData: ModuleDataResponse?
)

fun UserModuleResponse.toUserModuleData() = UserModuleData(
    id = this.id ?: 0,
    status = this.status.orEmpty(),
    moduleData = this.moduleData?.toModuleDataData()
)
