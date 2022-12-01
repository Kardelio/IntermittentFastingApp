package com.example.intermittentfasting.di

import android.content.Context
import androidx.room.Room
import com.example.intermittentfasting.db.FastDao
import com.example.intermittentfasting.db.FastDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        FastDatabase::class.java,
        "fast_database"
    ).build()


    @Provides
    @Singleton
    fun provideFastDao(db: FastDatabase): FastDao {
        return db.provideDao()
    }


}