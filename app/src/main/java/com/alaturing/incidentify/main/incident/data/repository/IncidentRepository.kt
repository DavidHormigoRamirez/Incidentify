package com.alaturing.incidentify.main.incident.data.repository

import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    suspend fun readAll():Result<List<Incident>>
    fun observeAll(): Flow<Result<List<Incident>>>
}