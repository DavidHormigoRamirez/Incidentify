package com.alaturing.incidentify.main.incident.data.remote

import com.alaturing.incidentify.main.incident.model.Incident

interface IncidentRemoteDatasource {


    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
}
