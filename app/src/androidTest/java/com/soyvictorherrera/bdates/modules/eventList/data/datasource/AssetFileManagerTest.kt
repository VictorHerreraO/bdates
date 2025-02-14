package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetFileManager
import java.io.FileNotFoundException
import org.junit.Before
import org.junit.Test

class AssetFileManagerTest {

    private lateinit var assetFileManager: AssetFileManager

    private val assetFileName = "asset_test.txt"
    private val expectedFileContent = "file content"

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        assetFileManager = AssetFileManager(assets = context.assets)
    }

    @Test
    fun open_file_as_string() {
        val content = assetFileManager.openAsString(assetFileName)

        assertThat(content).isNotNull()
        assertThat(content).isNotEmpty()
        assertThat(content).isEqualTo(expectedFileContent)
    }

    @Test(expected = FileNotFoundException::class)
    fun open_non_existent_file_as_string_should_fail() {
        assetFileManager.openAsString("non-existent.file")
    }

}
