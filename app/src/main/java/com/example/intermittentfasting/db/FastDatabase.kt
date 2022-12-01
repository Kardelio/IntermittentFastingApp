package com.example.intermittentfasting.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.intermittentfasting.model.Fast

@Database(
    entities = [Fast::class],
    version = 1,
    exportSchema = false
)
abstract class FastDatabase: RoomDatabase(){
    abstract fun provideDao(): FastDao
}