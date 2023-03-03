package com.soyvictorherrera.bdates.modules.circles.data.mapper

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.test.data.circleEntity
import com.soyvictorherrera.bdates.test.data.circleModel
import org.junit.Test

class CircleEntityToModelMapperTest {

    private val subjectUnderTest = CircleEntityToModelMapper

    @Test
    fun `assert map circle entity to model`() {
        val entity = circleEntity()
        val expected = circleModel()

        val result = subjectUnderTest.map(entity)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `assert reverse map circle model to entity`() {
        val model = circleModel()
        val expected = circleEntity()

        val result = subjectUnderTest.reverseMap(model)

        assertThat(result).isEqualTo(expected)
    }
}