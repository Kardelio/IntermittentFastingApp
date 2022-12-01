package com.example.intermittentfasting.data

import com.example.intermittentfasting.db.FastDao
import com.example.intermittentfasting.model.Fast
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FastRepository {

    suspend fun updateFast(fast: Fast)

    suspend fun getMostRecentFast(): Fast?

    suspend fun getCurrentOrLast(): Flow<Fast?>

    suspend fun getAllPastFasts(): Flow<List<Fast>>

    suspend fun deleteSpecificFast(fastId: Int)
}

class FastRepositoryImpl @Inject constructor(

    private val dao: FastDao
) : FastRepository {


    override suspend fun updateFast(fast: Fast) {
        dao.insertFast(fast)
    }

    override suspend fun getMostRecentFast(): Fast? {
        return dao.getMostRecentFast()
    }

    override suspend fun getCurrentOrLast(): Flow<Fast?> {
        return dao.getCurrentOrLast()
    }

    override suspend fun getAllPastFasts(): Flow<List<Fast>> {
        return dao.getAllPastFasts()
    }

    override suspend fun deleteSpecificFast(fastId: Int) {
        dao.deleteSpecificFast(fastId)
    }
}