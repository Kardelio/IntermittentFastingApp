package com.example.intermittentfasting.di

import android.content.Context
import com.example.intermittentfasting.domain.FastUseCase
import com.example.intermittentfasting.domain.FastUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule(){

    @Provides
    fun provideLocale(@ApplicationContext context: Context): Locale{
       return context.resources.configuration.locales[0]
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface InternalModule{

        @Binds
        fun getContext(@ApplicationContext context: Context): Context

        @Binds
        fun getCurrentFastUseCase(currentFastUseCaseImpl: FastUseCaseImpl): FastUseCase
    }

}