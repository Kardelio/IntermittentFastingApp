package com.example.intermittentfasting.data

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

interface FileRepository {

    fun readFile(): String

    fun writeToFile(contents: String)

}

/**
 * to write and read file?
 */

class FileRepositoryImpl @Inject constructor() : FileRepository {

    override fun readFile(): String {
        val downloads =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var outData = ""
        try {
            outData =
                FileInputStream(File(downloads, FILE_NAME)).readBytes().toString(Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return outData
    }

    private fun checkIfStorageIsReadOnly(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    private fun checkIfStorageIsAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    override fun writeToFile(contents: String) {
        if (!checkIfStorageIsAvailable() || checkIfStorageIsReadOnly()) {
            Log.d("BK", "Cant write")
        } else {
            val downloads =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            try {
                val fos = FileOutputStream(File(downloads, FILE_NAME))
                fos.write(contents.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val FILE_NAME = "IFData.txt"
    }
}
