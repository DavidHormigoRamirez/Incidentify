package com.alaturing.incidentify.di

import android.content.Context
import androidx.room.Room
import com.alaturing.incidentify.incident.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Hilt [Module] to satisfy database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /**
     * Function to provide the incident local database
     * @param context Context of the application where the database will be created
     * @return Created database
     */
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "incident-db"
        ).build()
    }

    /**
     * Function to provide the data access object of the database
     * @param database Application database
     * @return Data access object for incidents
     */
    @Provides
    fun provideIncidentDao(database: AppDatabase) = database.incidentDao()

}