package com.soyvictorherrera.bdates.core.event

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationEventTest {

    @Test
    fun `assert consume updates consumed field in event`() {
        val subjectUnderTest = NavigationEvent.NavigateBack()

        assertFalse(subjectUnderTest.consumed)

        subjectUnderTest.consume { /* No-Op */ }

        assertTrue(subjectUnderTest.consumed)
    }

}