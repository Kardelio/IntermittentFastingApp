package com.example.intermittentfasting.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.intermittentfasting.model.Fast

@Database(
    entities = [Fast::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class FastDatabase : RoomDatabase() {
    abstract fun provideDao(): FastDao
}