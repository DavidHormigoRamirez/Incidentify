package com.alaturing.incidentify.main.incident.data.local

import android.net.Uri
import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow

interface IncidentLocalDatasource {

    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
    suspend fun readOne(id:Long):Result<Incident>
    /**
     * @return Resultado de lista de incidentes
     */
    suspend fun createOne(incident:Incident):Result<Incident>

    suspend fun createOne(
        description:String,
        latitude:Double?,
        longitude:Double?,
        evidence: Uri?,
    ):Result<Incident>
    fun observeAll(): Flow<Result<List<Incident>>>

    suspend fun readUnsynched():Result<List<Incident>>

    suspend fun markAsSynched(incident:Incident):Result<Incident>
}