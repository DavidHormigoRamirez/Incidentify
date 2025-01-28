package com.alaturing.incidentify.main.incident.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    @Update
    suspend fun updateIncident(incident: IncidentEntity)

    @Insert
    suspend fun insertIncident(incident: IncidentEntity)

    @Query("SELECT * FROM incident")
    suspend fun readAllIncidents():List<IncidentEntity>
    @Query("SELECT * FROM incident")
    fun observeIncidents(): Flow<List<IncidentEntity>>
    @Query("SELECT * FROM incident WHERE id = :id")
    suspend fun readOne(id:Int):IncidentEntity?

    @Query("SELECT * FROM incident WHERE isSynch = :isSynch")
    suspend fun readBySynch(isSynch:Boolean):List<IncidentEntity>

}