package com.example.intermittentfasting.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

   @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}