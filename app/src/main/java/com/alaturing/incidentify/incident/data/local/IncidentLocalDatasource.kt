package com.alaturing.incidentify.incident.data.local

import android.net.Uri
import com.alaturing.incidentify.incident.model.Incident
import kotlinx.coroutines.flow.Flow

/**
 * Interface for local data sources
 */
interface IncidentLocalDatasource {

    suspend fun createAll(incidents:List<Incident>)
    suspend fun readAll():Result<List<Incident>>
    suspend fun readOne(id:Long):Result<Incident>
    suspend fun createOne(incident:Incident):Result<Incident>
    suspend fun createOne(
        description:String,
        latitude:Double?,
        longitude:Double?,
        evidence: Uri?,
    ):Result<Incident>
    fun observeAll(): Flow<Result<List<Incident>>>
    suspend fun readUnsynchronized():Result<List<Incident>>
    suspend fun markAsSynchronized(incident:Incident):Result<Incident>
}