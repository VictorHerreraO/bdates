package com.soyvictorherrera.bdates.modules.circles.data.preferences

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.test.fakes.InMemoryKeyValueStore
import org.junit.After
import org.junit.Before
import org.junit.Test

class CirclePreferencesTest {

    private var keyValueStore = InMemoryKeyValueStore()

    private lateinit var subjectUnderTest: CirclePreferences

    @Before
    fun setup() {
        subjectUnderTest = CirclePreferences(keyValueStore)
    }

    @After
    fun cleanup() {
        keyValueStore.clear()
    }

    @Test
    fun `assert isLocalCircleCreated set and get`() {
        val initial = subjectUnderTest.isLocalCircleCreated
        assertThat(initial).isFalse()

        subjectUnderTest.isLocalCircleCreated = true

        val final = subjectUnderTest.isLocalCircleCreated
        assertThat(final).isTrue()
    }

    @Test
    fun `assert localCircleId set and get`() {
        val expected = "foo"

        val initial = subjectUnderTest.localCircleId
        assertThat(initial).isNull()

        subjectUnderTest.localCircleId = expected

        val final = subjectUnderTest.localCircleId
        assertThat(final).isEqualTo(expected)
    }
}

