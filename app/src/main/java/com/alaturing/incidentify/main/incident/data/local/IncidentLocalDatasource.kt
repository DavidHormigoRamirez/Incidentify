package com.alaturing.incidentify.main.incident.data.local

import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow

interface IncidentLocalDatasource {

    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
    suspend fun readOne(id:Int):Result<Incident>
    /**
     * @return Resultado de lista de incidentes
     */
    suspend fun createOne(incident:Incident):Result<Incident>

    fun observeAll(): Flow<Result<List<Incident>>>
}