package com.soyvictorherrera.bdates.core.date

import com.google.common.truth.Truth.assertThat
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DateProviderTest {

    @Mock
    private lateinit var mockFormatters: DateFormattersContract

    private lateinit var subjectUnderTest: DateProvider

    private val today = LocalDateTime.of(
        2023, 7, 1, 12, 0
    )

    private val zoneId = ZoneId.systemDefault()

    private val testClock = Clock.fixed(today.atZone(zoneId).toInstant(), zoneId)

    @Before
    fun setUp() {
        subjectUnderTest = DateProvider(
            clock = testClock,
            formatters = mockFormatters
        )
    }

    @Test
    fun `assert current date is today`() {
        val expected = today.toLocalDate()

        val result = subjectUnderTest.currentLocalDate

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `assert current date time is today`() {
        val expected = today

        val result = subjectUnderTest.currentLocalDateTime

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `assert format date as day output`() {
        val dayFormatter = DateTimeFormatter.ofPattern("EEEE d", Locale.US)
        val date = today.toLocalDate()
        val expectedOutput = "Saturday 1"

        whenever(mockFormatters.dayFormatter).thenReturn(dayFormatter)

        val result = subjectUnderTest.formatDateAsDay(date)

        assertThat(result).isEqualTo(expectedOutput)
    }

}