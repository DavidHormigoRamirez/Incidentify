package com.alaturing.incidentify.main.incident.data.local

import android.net.Uri
import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow

interface IncidentLocalDatasource {

    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
    /**
     * @return Resultado de lista de incidentes
     */
    suspend fun createOne(incident:Incident):Result<Incident>

    fun observeAll(): Flow<List<Incident>>
}