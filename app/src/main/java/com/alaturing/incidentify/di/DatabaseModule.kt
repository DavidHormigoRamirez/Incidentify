package com.alaturing.incidentify.di

import android.content.Context
import androidx.room.Room
import com.alaturing.incidentify.main.incident.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "incident-db"
        ).build()
    }

    @Provides
    fun provideIncidentDao(database: AppDatabase) = database.incidentDao()

}