package com.group2.sinow.data.local

import io.mockk.MockKAnnotations
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IntroPageDataSourceImplTest {

    private lateinit var dataSource: IntroPageDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = IntroPageDataSourceImpl()
    }

    @Test
    fun getIntroPageData() {
        val introSliderItems = dataSource.getIntroPageData()
        assertEquals(3, introSliderItems.size)
        assertEquals("SINOW", introSliderItems[0].title)
    }
}
