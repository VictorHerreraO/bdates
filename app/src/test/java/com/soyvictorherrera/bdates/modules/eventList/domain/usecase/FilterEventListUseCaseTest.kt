package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import org.junit.Before
import org.junit.Test

class FilterEventListUseCaseTest {

    private lateinit var useCase: FilterEventListUseCase

    @Before
    fun setup() {
        useCase = FilterEventListUseCase()
    }

    @Test
    fun filter_event_by_name() {
        val expectedName = "expected event name"
        val expectedEvent = eventA().copy(name = expectedName)
        val nonMatchingEvent = eventB()
        val params = FilterEventListArgs(
            eventList = listOf(expectedEvent, nonMatchingEvent),
            query = "expected"
        )

        val result = useCase.invoke(params)

        assertThat(result.isSuccess).isTrue()
        val filtered = result.getOrThrow()
        assertThat(filtered).contains(expectedEvent)
        assertThat(filtered).doesNotContain(nonMatchingEvent)
    }

    @Test
    fun filter_event_by_name_with_empty_query_returns_all() {
        val expectedList = expectedEventList()
        val params = FilterEventListArgs(
            eventList = expectedList,
            query = ""
        )

        val result = useCase.invoke(params)

        assertThat(result.isSuccess).isTrue()
        val filtered = result.getOrThrow()
        assertThat(filtered).isNotEmpty()
        assertThat(filtered).hasSize(expectedList.size)
        assertThat(filtered).isEqualTo(expectedList)
    }

    @Test
    fun filter_event_by_name_with_non_matching_query_returns_none() {
        val expectedList = expectedEventList()
        val params = FilterEventListArgs(
            eventList = expectedList,
            query = "not found"
        )

        val result = useCase.invoke(params)

        assertThat(result.isSuccess).isTrue()
        val filtered = result.getOrThrow()
        assertThat(filtered).isEmpty()
    }

    private fun eventA() = Event(
        id = "event-a",
        name = "event name a",
        dayOfMonth = 1,
        monthOfYear = 1,
        year = 1970
    )


    private fun eventB() = Event(
        id = "event-b",
        name = "event name b",
        dayOfMonth = 2,
        monthOfYear = 2,
        year = 1971
    )

    private fun expectedEventList() = listOf(
        eventA(), eventB()
    )

}
