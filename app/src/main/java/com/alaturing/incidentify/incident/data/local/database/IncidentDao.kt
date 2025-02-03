package com.alaturing.incidentify.incident.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    @Update
    suspend fun updateIncident(incident: IncidentEntity):Int
    //@Update(entity = IncidentEntity::class)
    //suspend fun updateSynchronized(synchronized: IncidentSynchronized):Int

    @Insert
    suspend fun insertIncident(incident: IncidentEntity):Long

    @Insert(entity = IncidentEntity::class)
    suspend fun insertIncident(newIncident:NewIncidentEntity):Long

    @Query("SELECT * FROM incident")
    suspend fun readAllIncidents():List<IncidentEntity>
    @Query("SELECT * FROM incident")
    fun observeIncidents(): Flow<List<IncidentEntity>>
    @Query("SELECT * FROM incident WHERE localId = :id")
    suspend fun readOne(id:Long):IncidentEntity?

    @Query("SELECT * FROM incident WHERE isSynch = :isSynch")
    suspend fun readBySynch(isSynch:Boolean):List<IncidentEntity>

}