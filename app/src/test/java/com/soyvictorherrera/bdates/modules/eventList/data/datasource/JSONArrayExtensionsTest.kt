package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.google.common.truth.Truth.assertThat
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

class JSONArrayExtensionsTest {

    private lateinit var array: JSONArray

    @Before
    fun setup() {
        array = JSONArray()
            .put(true)
            .put(1f)
            .put(2)
            .put(3L)
            .put(JSONObject())
    }

    @Test
    fun action_called_for_each_element_in_array() {
        var count = 0
        array.forEach { count++ }

        assertThat(count).isEqualTo(array.length())
    }
}
