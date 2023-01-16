package com.soyvictorherrera.bdates.core.resource

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ResourceManagerTest {

    private lateinit var resourceManager: ResourceManager

    @Before
    fun setup() {
        // Use test context to access test resources
        val context = InstrumentationRegistry.getInstrumentation().context
        resourceManager = ResourceManager(context)

    }

    @Test
    fun get_string_resource() {
        val string = resourceManager.getString(identifier = "test_string_no_args")

        assertThat(string).isNotNull()
        assertThat(string).isNotEmpty()
        assertThat(string).isEqualTo("test string")
    }

    @Test
    fun get_string_resource_with_args() {
        val arg0 = 1
        val arg1 = "apple"
        val arg2 = true
        val string = resourceManager.getString(
            identifier = "test_string_with_args",
            arg0,
            arg1,
            arg2
        )

        assertThat(string).isNotNull()
        assertThat(string).isNotEmpty()
        assertThat(string).isEqualTo("test string #$arg0 = $arg1 is $arg2")
    }


    @Test
    fun get_string_resource_with_wrong_args_return_empty() {
        val arg = "apple"
        val string = resourceManager.getString(identifier = "test_string_with_int_arg", arg)

        assertThat(string).isNotNull()
        assertThat(string).isEmpty()
    }

    @Test
    fun get_string_resource_non_existent_returns_empty() {
        val string = resourceManager.getString(identifier = "test_string_non_existent")

        assertThat(string).isNotNull()
        assertThat(string).isEmpty()
    }

}
