package com.group2.sinow.data.local

import com.group2.sinow.R
import com.group2.sinow.model.introslider.IntroSliderItem

interface IntroPageDataSource {
    fun getIntroPageData(): List<IntroSliderItem>
}

class IntroPageDataSourceImpl() : IntroPageDataSource {
    override fun getIntroPageData(): List<IntroSliderItem> {
        return listOf(
            IntroSliderItem("SINOW", "Sinau dari pengalaman", R.drawable.introslider_icon1),
            IntroSliderItem("SINOW", "Sinau dari mentor", R.drawable.introslider_icon2),
            IntroSliderItem("SINOW", "Sinau darimana saja", R.drawable.introslider_icon3)
        )
    }
}
