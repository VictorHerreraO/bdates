package com.soyvictorherrera.bdates.modules.notifications.data.preferences

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.date.DateFormattersContract
import com.soyvictorherrera.bdates.test.fakes.InMemoryKeyValueStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.junit.After
import org.junit.Before
import org.junit.Test


class NotificationPreferencesTest {

    private val store = InMemoryKeyValueStore()

    private val dateFormatters = object : DateFormattersContract {
        override val dayFormatter get() = null!!
        override val ddMMYYYYFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    private lateinit var subjectUnderTest: NotificationPreferences

    private val today = LocalDate.of(2023, 5, 6)

    @Before
    fun setup() {
        subjectUnderTest = NotificationPreferences(
            store = store,
            dateFormatters = dateFormatters,
        )
    }

    @After
    fun cleanup() {
        store.clear()
    }

    @Test
    fun `assert lastShownNotificationDate set and get`() {
        val expected = today

        val initial = subjectUnderTest.lastShownNotificationDate
        assertThat(initial).isNull()

        subjectUnderTest.lastShownNotificationDate = expected

        val final = subjectUnderTest.lastShownNotificationDate
        assertThat(final).isEqualTo(expected)
    }
}