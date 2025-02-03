package com.alaturing.incidentify.incident.data.remote

import android.net.Uri
import com.alaturing.incidentify.incident.model.Incident

interface IncidentRemoteDatasource {


    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
    /**
     * @return Resultado de lista de incidentes
     */
    suspend fun createOne(
        localId:Long,
        description:String,
                          evidence: Uri?,
                          latitude:Double?,
                          longitude:Double?,):Result<Incident>
}
