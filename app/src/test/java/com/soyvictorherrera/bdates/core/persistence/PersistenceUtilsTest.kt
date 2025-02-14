package com.soyvictorherrera.bdates.core.persistence

import com.google.common.truth.Truth.assertThat
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Test

class PersistenceUtilsTest {

    @Test
    fun `assert that random uuid is valid`() {
        val result = randomUUID()

        assertThat(result).isNotEmpty()
        UUID.fromString(result)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `assert that add source adds result to list`() {
        val source = "source"

        TestScope().launch {
            val result = mutableListOf<Char>().addSource(source) {
                toCharArray().toList()
            }

            source.forEachIndexed { index, char ->
                assertThat(result[index]).isEqualTo(char)
            }
        }
    }
}
