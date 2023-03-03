package com.soyvictorherrera.bdates.modules.circles.data.preferences

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.persistence.InMemoryKeyValueStore
import com.soyvictorherrera.bdates.core.persistence.KeyValueStoreContract
import org.junit.Before
import org.junit.Test

class CirclePreferencesTest {

    private lateinit var keyValueStore: KeyValueStoreContract

    private lateinit var subjectUnderTest: CirclePreferences

    @Before
    fun setup() {
        keyValueStore = InMemoryKeyValueStore()

        subjectUnderTest = CirclePreferences(keyValueStore)
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

