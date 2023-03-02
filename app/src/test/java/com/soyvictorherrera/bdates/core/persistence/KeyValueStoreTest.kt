package com.soyvictorherrera.bdates.core.persistence

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class KeyValueStoreTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor

    private lateinit var subjectUnderTest: KeyValueStore

    @Before
    fun setup() {
        preferencesEditor = mockk {
            every { apply() } just runs
            every { putString(any(), any()) } returns this
            every { putBoolean(any(), any()) } returns this
        }
        sharedPreferences = mockk {
            every { edit() } returns preferencesEditor
        }

        subjectUnderTest = KeyValueStore(
            sharedPreferences = sharedPreferences
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect exception on unsupported get`() {
        val unsupportedKeyType = Key("unsupported", Any::class)

        every { sharedPreferences.contains(any()) } returns true

        subjectUnderTest[unsupportedKeyType]
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect exception on unsupported set`() {
        val unsupportedKeyType = Key("unsupported", Any::class)

        subjectUnderTest[unsupportedKeyType] = Any()
    }

    @Test
    fun `verify set string`() {
        val key = stringKey("string-key")
        val expectedValue = "value"

        subjectUnderTest[key] = expectedValue

        verify(exactly = 1) { preferencesEditor.putString(key.name, expectedValue) }
    }

    @Test
    fun `assert get string`() {
        val key = stringKey("string-key")
        val expectedValue = "value"

        every { sharedPreferences.contains(key.name) } returns true
        every { sharedPreferences.getString(key.name, any()) } returns expectedValue

        val result = subjectUnderTest[key]

        assertEquals(expectedValue, result)
    }

    @Test
    fun `assert get string returns null`() {
        val key = stringKey("string-key")

        every { sharedPreferences.contains(key.name) } returns false

        val result = subjectUnderTest[key]

        assertNull(result)
    }

    @Test
    fun `verify set boolean`() {
        val key = booleanKey("boolean-key")
        val expectedValue = true

        subjectUnderTest[key] = expectedValue

        verify(exactly = 1) { preferencesEditor.putBoolean(key.name, expectedValue) }
    }

    @Test
    fun `assert get boolean`() {
        val key = booleanKey("boolean-key")
        val expectedValue = true

        every { sharedPreferences.contains(key.name) } returns true
        every { sharedPreferences.getBoolean(key.name, any()) } returns expectedValue

        val result = subjectUnderTest[key]

        assertEquals(expectedValue, result)
    }

    @Test
    fun `assert get boolean returns null`() {
        val key = booleanKey("boolean-key")
        every { sharedPreferences.contains(key.name) } returns false

        val result = subjectUnderTest[key]

        assertNull(result)
    }

}