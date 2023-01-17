package com.example.intermittentfasting.di

import android.content.Context
import android.content.SharedPreferences
import com.example.intermittentfasting.alarm.AlarmScheduler
import com.example.intermittentfasting.alarm.AlarmSchedulerImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("intermittentfasting", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface InnerAppModule {
        @Binds
        fun provideAlarmScheduler(scheduler: AlarmSchedulerImpl): AlarmScheduler
    }
}