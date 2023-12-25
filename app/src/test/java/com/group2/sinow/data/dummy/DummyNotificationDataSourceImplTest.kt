package com.group2.sinow.data.dummy

import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test


class DummyNotificationDataSourceImplTest {

    private lateinit var dataSource: DummyNotificationDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = DummyNotificationDataSourceImpl()
    }

    @Test
    fun `getNotificationData should return list of notification`() {
        val result = dataSource.getNotificationData()
        assert(result.isNotEmpty())
    }

}