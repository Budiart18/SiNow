package com.group2.sinow.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.mask.SkeletonShimmerDirection
import com.group2.sinow.R

class SkeletonConfigWrapper(private val appContext: Context) {

    fun customSkeletonConfig() = SkeletonConfig(
        maskColor = ContextCompat.getColor(appContext, R.color.app_color_on_primary),
        maskCornerRadius = 20.0F,
        showShimmer = true,
        shimmerColor = ContextCompat.getColor(appContext, R.color.app_color_primary),
        shimmerDurationInMillis = 1500,
        shimmerDirection = SkeletonShimmerDirection.LEFT_TO_RIGHT,
        shimmerAngle = 45
    )

}