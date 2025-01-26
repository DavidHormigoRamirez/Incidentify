package com.alaturing.incidentify.main.incident.data.repository

import android.net.Uri
import com.alaturing.incidentify.main.incident.data.local.IncidentLocalDatasource
import com.alaturing.incidentify.main.incident.model.Incident
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncidentRepositoryDefault @Inject constructor(
    private val remote: IncidentRemoteDatasource,
    private val local: IncidentLocalDatasource
):IncidentRepository {
    override suspend fun readAll(): Result<List<Incident>> {
        val result = remote.readAll()

        return result
    }

    override suspend fun readOne(id:Int): Result<Incident> {
        return local.readOne(id)
    }

    override suspend fun createOne(
        description: String,
        latitude: Double?,
        longitude: Double?,
        evidence: Uri?
    ): Result<Incident> {
        // Subimos el incidente al backend
        val result = remote.createOne(description,evidence,latitude,longitude)
        if (result.isSuccess) {
            val incident = result.getOrNull()
            /**val copyIncident = incident?.copy(
                photoUri = evidence
            )
            copyIncident?.let {
                local.createOne(it)
            }*/
            incident?.let {
                local.createOne(it)
            }
        }
        return result
    }

    override fun observeAll(): Flow<Result<List<Incident>>> {

        return local.observeAll()


        /**val incidents = flow<Result<List<Incident>>> {
            val result = remote .readAll()
            emit(result)
        }
        return incidents*/
    }
}