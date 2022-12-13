package com.example.intermittentfasting.domain

import android.util.Log
import com.example.intermittentfasting.data.FastRepository
import com.example.intermittentfasting.data.FileRepository
import com.example.intermittentfasting.model.Fast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FileUseCase {
    fun writeFileWithFasts()

    suspend fun readInFileWithFasts(): List<Fast>

}

class FileUseCaseImpl @Inject constructor(
    private val fileRepository: FileRepository,
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val gson: Gson,
    private val fastRepo: FastRepository
) : FileUseCase {
    override fun writeFileWithFasts() {

        val j = coroutineScope.launch {
            val fasts = fastRepo.getAllPastFastsOnce()

            val gsonned = gson.toJson(fasts)

            fileRepository.writeToFile(gsonned)

        }.invokeOnCompletion {
            Log.d("BK", "Completion")
        }
    }

    override suspend fun readInFileWithFasts(): List<Fast> {
        val data = fileRepository.readFile()
        val myType = object : TypeToken<List<Fast>>() {}.type

        val cleaned = gson.fromJson<List<Fast>>(data, myType)
        Log.d("BK", "Fasts: ${cleaned}")
        return cleaned
    }
}