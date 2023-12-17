package com.group2.sinow.data.network.api.model.detailcourse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.detailcourse.CourseDetailData

@Keep
data class CourseResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("videoPreviewUrl")
    val videoPreviewUrl: String?,
    @SerializedName("level")
    val level: String?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("categoryId")
    val categoryId: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("classCode")
    val classCode: String?,
    @SerializedName("totalModule")
    val totalModule: Int?,
    @SerializedName("totalDuration")
    val totalDuration: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("promo")
    val promo: Int?,
    @SerializedName("totalUser")
    val totalUser: Int?,
    @SerializedName("courseBy")
    val courseBy: String?,
    @SerializedName("createdBy")
    val createdBy: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("category")
    val category: CategoryResponse?,
    @SerializedName("courseCreator")
    val courseCreator: CourseCreatorResponse?,
    @SerializedName("benefits")
    val benefits: List<BenefitResponse>?,
    @SerializedName("chapters")
    val chapters: List<ChapterResponse>?
)

fun CourseResponse.toCourseDetailData() = CourseDetailData(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    imageUrl = this.imageUrl.orEmpty(),
    videoPreviewUrl = this.videoPreviewUrl.orEmpty(),
    level = this.level.orEmpty(),
    rating = this.rating ?: 0.0,
    categoryId = this.categoryId ?: 0,
    description = this.description.orEmpty(),
    classCode = this.classCode.orEmpty(),
    totalModule = this.totalModule ?: 0,
    totalDuration = this.totalDuration ?: 0,
    type = this.type.orEmpty(),
    price = this.price ?: 0,
    promo = this.promo ?: 0,
    totalUser = this.totalUser ?: 0,
    courseBy = this.courseBy.orEmpty(),
    createdBy = this.createdBy ?: 0,
    createdAt = this.createdAt.orEmpty(),
    updatedAt = this.updatedAt.orEmpty(),
    category = this.category?.toCategoryData(),
    courseCreator = this.courseCreator?.toCourseCreatorData(),
    benefits = this.benefits?.map { it.toBenefitData() },
    chapters = this.chapters?.map { it.toChapterData() }
)