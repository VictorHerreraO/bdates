package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.test.data.eventModelBar
import com.soyvictorherrera.bdates.test.data.eventModelFoo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FilterEventListUseCaseTest {

    private lateinit var useCase: FilterEventListUseCase

    @Before
    fun setup() {
        useCase = FilterEventListUseCase()
    }

    @Test
    fun filter_event_by_name() = runTest {
        val expectedName = "expected event name"
        val expectedEvent = eventModelFoo().copy(name = expectedName)
        val nonMatchingEvent = eventModelBar()
        val params = FilterEventListArgs(
            eventList = listOf(expectedEvent, nonMatchingEvent),
            query = "expected"
        )

        val result = useCase.execute(params)

        assertThat(result.isSuccess).isTrue()
        val filtered = result.getOrThrow()
        assertThat(filtered).contains(expectedEvent)
        assertThat(filtered).doesNotContain(nonMatchingEvent)
    }

    @Test
    fun filter_event_by_name_with_empty_query_returns_all() = runTest {
        val expectedList = listOf(eventModelFoo(), eventModelBar())
        val params = FilterEventListArgs(
            eventList = expectedList,
            query = ""
        )

        val result = useCase.execute(params)

        assertThat(result.isSuccess).isTrue()
        val filtered = result.getOrThrow()
        assertThat(filtered).isNotEmpty()
        assertThat(filtered).hasSize(expectedList.size)
        assertThat(filtered).isEqualTo(expectedList)
    }

    @Test
    fun filter_event_by_name_with_non_matching_query_returns_none() = runTest {
        val expectedList = listOf(eventModelFoo(), eventModelBar())
        val params = FilterEventListArgs(
            eventList = expectedList,
            query = "not found"
        )

        val result = useCase.execute(params)

        assertThat(result.isSuccess).isTrue()
        val filtered = result.getOrThrow()
        assertThat(filtered).isEmpty()
    }

}
