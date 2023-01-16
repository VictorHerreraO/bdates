package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import android.content.res.AssetManager
import java.io.IOException

class AssetFileManager(
    private val assets: AssetManager
): AssetFileManagerContract {

    @Throws(IOException::class)
    override fun openAsString(fileName: String): String {
        val inputStream = assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)

        inputStream.read(buffer)
        inputStream.close()

        return String(buffer, Charsets.UTF_8)
    }

}
