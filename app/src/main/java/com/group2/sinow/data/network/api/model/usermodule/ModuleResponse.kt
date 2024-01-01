package com.group2.sinow.data.network.api.model.usermodule

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.usermodule.ModuleData

@Keep
data class ModuleResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("no")
    val no: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("videoUrl")
    val videoUrl: String?
)

fun ModuleResponse.toModuleData() = ModuleData(
    id = this.id ?: 0,
    no = this.no ?: 0,
    name = this.name.orEmpty(),
    videoUrl = this.videoUrl.orEmpty()
)
