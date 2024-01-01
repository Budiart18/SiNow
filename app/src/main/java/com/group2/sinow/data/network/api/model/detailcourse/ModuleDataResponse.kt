package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.ModuleDataData

@Keep
data class ModuleDataResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("no")
    val no: Int?,
    @SerializedName("name")
    val name: String?
)

fun ModuleDataResponse.toModuleDataData() = ModuleDataData(
    id = this.id ?: 0,
    no = this.no ?: 0,
    name = this.name.orEmpty()
)
