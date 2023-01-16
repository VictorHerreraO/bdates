package com.soyvictorherrera.bdates.core.arch

import com.google.common.truth.Truth.*
import org.junit.Before
import org.junit.Test

class MapperTest {

    private lateinit var mapper: Mapper<Int, String>

    @Before
    fun setup() {
        mapper = MapperTestImpl
    }

    @Test
    fun map_collection() {
        val numbers = listOf(1, 2, 3)
        val result = mapper.mapCollection(numbers)

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(numbers.size)
        assertThat(result).contains("1")
        assertThat(result).contains("2")
        assertThat(result).contains("3")
    }

    @Test
    fun reverse_map_collection() {
        val numbers = listOf("1", "2", "3")
        val result = mapper.reverseMapCollection(numbers)

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(numbers.size)
        assertThat(result).contains(1)
        assertThat(result).contains(2)
        assertThat(result).contains(3)
    }

}
