package com.alaturing.incidentify.incident.data.repository

import android.net.Uri
import com.alaturing.incidentify.incident.model.Incident
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    suspend fun refreshIncidents()

    suspend fun readAll():Result<List<Incident>>

    suspend fun readOne(id:Long):Result<Incident>

    suspend fun createOne(description:String,
                          latitude: Double? = null,
                          longitude: Double? = null,
                          evidence: Uri?,
                          ):Result<Incident>



    fun observeAll(): Flow<Result<List<Incident>>>
}