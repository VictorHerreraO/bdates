package com.soyvictorherrera.bdates.modules.eventList.data.mapper

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.test.data.event
import com.soyvictorherrera.bdates.test.data.eventEntity
import org.junit.Test


class EventEntityToModelMapperTest {

    private val subjectUnderTest = EventEntityToModelMapper

    @Test
    fun map() {
        val entity = eventEntity()
        val expected = event()

        val result = subjectUnderTest.map(entity)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun reverseMap() {
        val model = event()
        val expected = eventEntity()

        val result = subjectUnderTest.reverseMap(model)

        assertThat(result).isEqualTo(expected)
    }
}