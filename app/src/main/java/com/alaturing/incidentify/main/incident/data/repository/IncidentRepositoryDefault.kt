package com.alaturing.incidentify.main.incident.data.repository

import com.alaturing.incidentify.main.incident.model.Incident
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IncidentRepositoryDefault @Inject constructor(
    private val remote: IncidentRemoteDatasource
):IncidentRepository {
    override suspend fun readAll(): Result<List<Incident>> {
        val result = remote.readAll()
        return result
    }

    override fun observeAll(): Flow<Result<List<Incident>>> {
        val incidents = flow<Result<List<Incident>>> {
            val result = remote .readAll()
            emit(result)
        }
        return incidents
    }
}