package com.example.intermittentfasting.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.intermittentfasting.model.Fast
import kotlinx.coroutines.flow.Flow

@Dao
interface FastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFast(fast: Fast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleFasts(listOfFasts: List<Fast>)

    @Query("SELECT * FROM fasts WHERE manuallyEnteredPastFast = 0 ORDER BY id DESC LIMIT 0, 1")
    suspend fun getMostRecentFast(): Fast?

    @Query("SELECT * FROM fasts WHERE manuallyEnteredPastFast = 0 ORDER BY id DESC LIMIT 0, 1")
    fun getCurrentOrLast(): Flow<Fast?>

    @Query("SELECT * FROM fasts WHERE endTimeUTC!='' ORDER BY endTimestamp DESC")
    fun getAllPastFasts(): Flow<List<Fast>>

    @Query("SELECT * FROM fasts WHERE endTimeUTC!='' ORDER BY endTimestamp DESC")
    suspend fun getAllPastFastsOnce(): List<Fast>

    @Query("DELETE FROM fasts WHERE id = :fastId")
    suspend fun deleteSpecificFast(fastId: Int)
}