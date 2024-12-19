package com.alaturing.incidentify.main.incident.data.remote

import com.alaturing.incidentify.common.remote.StrapiApi
import com.alaturing.incidentify.main.incident.model.Incident
import com.alaturing.incidentify.main.incident.data.remote.model.toModel
import javax.inject.Inject

/**
 * Implementación de [RemoteDatasource] para gestionar el consumo de una APi en Strapi
 */
class IncidentRemoteDatasourceStrapi @Inject constructor(
    private val api: StrapiApi
) : IncidentRemoteDatasource {


    /**
     * Lee todos los incidentes remotos
     */
    override suspend fun readAll(): Result<List<Incident>> {
        val response = api.incidentReadAll()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.data.toModel())
        }
        else {
            TODO("Implementar manejo de errores")
        }
    }
}