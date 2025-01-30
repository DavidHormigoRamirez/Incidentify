package com.alaturing.incidentify.main.incident.data.local


import android.net.Uri
import com.alaturing.incidentify.common.exception.IncidentNotFoundException
import com.alaturing.incidentify.common.exception.NoUnsynchedIncidentsException
import com.alaturing.incidentify.main.incident.data.local.database.IncidentDao
import com.alaturing.incidentify.main.incident.data.local.database.IncidentEntity
import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.util.Log
import com.alaturing.incidentify.common.exception.IncidentNotCreatedException
import com.alaturing.incidentify.main.incident.data.local.database.NewIncidentEntity

class IncidentLocalDataSourceRoom @Inject constructor(
    private val dao: IncidentDao
):IncidentLocalDatasource {

    override suspend fun readAll(): Result<List<Incident>> {
        val incidents = dao.readAllIncidents().map(IncidentEntity::toExternal)
        return Result.success(incidents)
    }

    override suspend fun readOne(id: Long): Result<Incident> {
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

    override suspend fun createOne(
        description: String,
        latitude: Double?,
        longitude: Double?,
        evidence: Uri?
    ): Result<Incident> {
        val id = dao.insertIncident(NewIncidentEntity(
            description = description,
            latitude = latitude,
            longitude = longitude,
            photoUri = evidence.toString(),
        ))
        val incident = dao.readOne(id)?.toExternal()
        return if (incident!=null) {
            Result.success(incident)
        } else {
            Result.failure(IncidentNotCreatedException())
        }


    }


    override fun observeAll(): Flow<Result<List<Incident>>> {
        return dao.observeIncidents().map {
            entities ->
            val incidents = entities.toExternal()
            Result.success(incidents)

        }
    }

    override suspend fun readUnsynched(): Result<List<Incident>> {
        val entities = dao.readBySynch(false)
        return if (entities.isEmpty()) {
            Result.failure(NoUnsynchedIncidentsException())
        }
        else {
            Result.success(entities.toExternal())
        }
    }

    override suspend fun markAsSynched(incident: Incident):Result<Incident> {
        val entity = incident.toEntity()
        val updatedEntity = entity.copy(
            isSynch = true
        )
        val isUpdated = dao.updateIncident(updatedEntity)
        if (isUpdated>0) {
            Log.d("DHR","Entity updated")
            Log.d("DHR",updatedEntity.toString())
            return Result.success(updatedEntity.toExternal())
        }
        else {
            Log.e("DHR","Entity not updated")
            return Result.failure(IncidentNotFoundException())
        }
    }
}


