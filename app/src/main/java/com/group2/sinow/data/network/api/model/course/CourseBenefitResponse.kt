package com.group2.sinow.data.network.api.model.course

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.course.CourseBenefit

@Keep
data class CourseBenefitResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("courseId")
    val courseId: Int?,
    @SerializedName("description")
    val description: String?
)

fun CourseBenefitResponse.toCourseBenefit() = CourseBenefit(
    id = this.id ?: 0,
    courseId = this.courseId ?: 0,
    description = this.description.orEmpty()
)

fun Collection<CourseBenefitResponse>.toCourseBenefitList() = this.map {
    it.toCourseBenefit()
}
