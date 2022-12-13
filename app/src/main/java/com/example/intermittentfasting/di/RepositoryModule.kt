package com.example.intermittentfasting.di

import com.example.intermittentfasting.data.FastRepository
import com.example.intermittentfasting.data.FastRepositoryImpl
import com.example.intermittentfasting.data.FileRepository
import com.example.intermittentfasting.data.FileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindFileRepo(repo: FileRepositoryImpl): FileRepository

    @Binds
    fun bindRepo(repo: FastRepositoryImpl): FastRepository
}