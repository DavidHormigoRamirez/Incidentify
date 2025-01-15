package com.alaturing.incidentify.main.incident.data.remote

import android.net.Uri
import com.alaturing.incidentify.main.incident.model.Incident

interface IncidentRemoteDatasource {


    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
    /**
     * @return Resultado de lista de incidentes
     */
    suspend fun createOne(description:String,evidence: Uri?):Result<Int>
}
