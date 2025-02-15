package com.alaturing.incidentify.main.incident.data.repository

import android.net.Uri
import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    suspend fun readAll():Result<List<Incident>>

    suspend fun readOne(id:Int):Result<Incident>

    suspend fun createOne(description:String,
                          latitude: Double? = null,
                          longitude: Double? = null,
                          evidence: Uri?,
                          ):Result<Incident>



    fun observeAll(): Flow<Result<List<Incident>>>
}