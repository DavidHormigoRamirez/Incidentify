package com.alaturing.incidentify.di

import android.content.Context
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Provides
    @Singleton
    fun provideWorkerManager(@ApplicationContext context:Context): WorkManager{
        return WorkManager.getInstance(context)

    }
}