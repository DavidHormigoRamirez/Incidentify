package com.alaturing.incidentify.incident.data.local


import android.net.Uri
import com.alaturing.incidentify.common.exception.IncidentNotFoundException
import com.alaturing.incidentify.common.exception.NoUnsynchedIncidentsException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.alaturing.incidentify.common.exception.IncidentNotCreatedException
import com.alaturing.incidentify.incident.data.local.database.IncidentDao
import com.alaturing.incidentify.incident.data.local.database.IncidentEntity
import com.alaturing.incidentify.incident.data.local.database.NewIncidentEntity
import com.alaturing.incidentify.incident.model.Incident

/**
 * [IncidentLocalDatasource] Implementation that uses Room as local persistence
 */
class IncidentLocalDataSourceRoom
@Inject constructor(
    private val dao: IncidentDao
):IncidentLocalDatasource {
    override suspend fun createAll(incidents: List<Incident>) {
        val entities = incidents.toEntity()
        dao.insertAll(entities)
    }

    /**
     * Read all incidents stored in the Room database
     * @return All local incidents
     */
    override suspend fun readAll(): Result<List<Incident>> {
        val incidents = dao.readAllIncidents().map(IncidentEntity::toExternal)
        return Result.success(incidents)
    }

    /**
     * Reads an incident by primary key
     * @return Success if the incident exists, Failure otherwise
     */
    override suspend fun readOne(id: Long): Result<Incident> {
        val entity: IncidentEntity? = dao.readOne(id)

        entity?.let {
            return@readOne Result.success(it.toExternal())
        }
        return Result.failure(IncidentNotFoundException())
    }

    /**
     * Creates a single incident
     * @return Success if created, Failure otherwise
     */
    override suspend fun createOne(incident: Incident): Result<Incident> {
        val localId = dao.insertIncident(incident.toEntity())
        return if (localId>0)
            Result.success(incident)
        else
            Result.failure(IncidentNotFoundException())
    }
    /**
     * Creates a single incident
     * @return Success if created, Failure otherwise
     */
    override suspend fun createOne(
        description: String,
        latitude: Double?,
        longitude: Double?,
        evidence: Uri?
    ): Result<Incident> {
        val id = dao.insertIncident(
            NewIncidentEntity(
            description = description,
            latitude = latitude,
            longitude = longitude,
            photoUri = evidence.toString(),
        )
        )
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

    override suspend fun readUnsynchronized(): Result<List<Incident>> {
        val entities = dao.readBySynch(false)
        return if (entities.isEmpty()) {
            Result.failure(NoUnsynchedIncidentsException())
        }
        else {
            Result.success(entities.toExternal())
        }
    }

    /**
     * Marks a single entity as synchronized
     * @return the updated entity, failure if not found
     */
    override suspend fun markAsSynchronized(incident: Incident):Result<Incident> {

        val entity = incident.toEntity()
        val synchedEntity = entity.copy(
            isSynch = true
        )
        val isUpdated = dao.updateIncident(synchedEntity)
        /*val isUpdated = dao.updateSynchronized(
            IncidentSynchronized(
                localId = incident.localId,
                isSynch = true
            )
        )*/

        return if (isUpdated>0) {
            Result.success(dao.readOne(incident.localId)!!.toExternal())
        } else {
            Result.failure(IncidentNotFoundException())
        }
    }
}


