package com.alaturing.incidentify.main.incident.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IncidentEntity::class], version = 1)
abstract class AppDatabase():RoomDatabase() {
    abstract  fun incidentDao(): IncidentDao
}