package com.alaturing.incidentify.main.incident.data.local

import com.alaturing.incidentify.common.exception.IncidentNotFoundException
import com.alaturing.incidentify.main.incident.data.local.database.IncidentDao
import com.alaturing.incidentify.main.incident.data.local.database.IncidentEntity
import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IncidentLocalDataSourceRoom @Inject constructor(
    private val dao: IncidentDao
):IncidentLocalDatasource {

    override suspend fun readAll(): Result<List<Incident>> {
        val incidents = dao.readAllIncidents().map(IncidentEntity::toExternal)
        return Result.success(incidents)
    }

    override suspend fun readOne(id: Int): Result<Incident> {
        val entity:IncidentEntity? = dao.readOne(id)

        entity?.let {
            return@readOne Result.success(it.toExternal())
        }
        return Result.failure(IncidentNotFoundException())
    }

    override suspend fun createOne(incident: Incident): Result<Incident> {
        dao.insertIncident(incident.toEntity())
        return Result.success(incident)
    }



    override fun observeAll(): Flow<Result<List<Incident>>> {
        return dao.observeIncidents().map {
            entities ->
            val incidents = entities.toExternal()
            Result.success(incidents)

        }
    }
}


