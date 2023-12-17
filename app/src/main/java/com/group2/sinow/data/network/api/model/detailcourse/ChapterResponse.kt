package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.ChapterData

@Keep
data class ChapterResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("no")
    val no: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("totalDuration")
    val totalDuration: Int?,
    @SerializedName("userModules")
    val userModules: List<UserModuleResponse>?
)
fun ChapterResponse.toChapterData() = ChapterData(
    id = this.id ?: 0,
    no = this.no ?: 0,
    name = this.name.orEmpty(),
    totalDuration = this.totalDuration ?: 0,
    userModules = this.userModules?.map { it.toUserModuleData() } ?: emptyList()
)