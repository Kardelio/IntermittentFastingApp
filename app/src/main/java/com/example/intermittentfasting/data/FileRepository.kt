package com.example.intermittentfasting.data

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.inject.Inject

interface FileRepository {

    fun readFile(): String

    fun writeToFile(contents: String)

    fun writeToFileNew(contents: String)

    fun downloadLocalFileToDownloads(cont: String)

}

/**
 * to write and read file?
 */

class FileRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : FileRepository {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun downloadLocalFileToDownloads(cont: String) {
        writeToExternalStorage(cont)
//        val dm = context.getSystemService(DownloadManager::class.java)
//        val req = DownloadManager.Request(Uri.fromFile(File("${context.filesDir}/${FILE_NAME}")))
//            .setMimeType("text/plain")
//            .setTitle("Butts")
//            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"outtt.txt")
//        dm.enqueue(req)

        /*
           OutputStream out;

   ContentResolver contentResolver = getContext().getContentResolver();

   ContentValues contentValues = new ContentValues();
   contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, localPath);
   contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
   contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS);
   Uri uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
   out = contentResolver.openOutputStream(uri);
         */
//        val dm = context.getSystemService(DownloadManager::class.java)
//        dm.addCompletedDownload(file.getName(), file.getName(), true, "application/json", file.getAbsolutePath(),file.length(),true);
        /*
        DownloadManager downloadManager = (DownloadManager)mainActivity.getSystemService(mainActivity.DOWNLOAD_SERVICE);
downloadManager.addCompletedDownload(file.getName(), file.getName(), true, "application/json", file.getAbsolutePath(),file.length(),true);
         */
    }

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

    // THIS works but with no notifiacatibn
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun writeToExternalStorage(contents: String) {
//        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        val fos = FileOutputStream(File(path, FILE_NAME))
//        fos.write(contents.toByteArray())
//        fos.close()
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "toat3.txt")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            URL("file://${context.filesDir}/${FILE_NAME}").openStream().use { input ->
                resolver.openOutputStream(uri).use { output ->
                    input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                }
            }
        }
    }

    override fun writeToFileNew(contents: String) {
        val path = context.filesDir
        val fos = FileOutputStream(File(path, FILE_NAME))
        fos.write(contents.toByteArray())
        fos.close()
    }

//    override fun test(context: Context) : String{
//
//       return context.getExternalFilesDir(null).toString()
//    }

    companion object {
        const val FILE_NAME = "IFData.txt"
    }
}
