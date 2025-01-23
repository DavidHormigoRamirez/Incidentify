package com.alaturing.incidentify.main.incident.data.local

import com.alaturing.incidentify.common.exception.IncidentNotFoundException
import com.alaturing.incidentify.main.incident.data.local.database.IncidentDao
import com.alaturing.incidentify.main.incident.data.local.database.IncidentEntity
import com.alaturing.incidentify.main.incident.model.Incident
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IncidentLocalDataSourceRoom @Inject constructor(
    private val dao: IncidentDao
):IncidentLocalDatasource {
    override suspend fun readAll(): Result<List<Incident>> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(id: Int): Result<Incident> {
        val entity:IncidentEntity? = dao.readOne(id)

        entity?.let {
            return@readOne Result.success(
                Incident(
                    id = it.id,
                    description = it.description,
                    solved = it.solved,
                    solved_at = it.solved_at,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    smallPhotoUrl = null,
                    thumbnailUrl = null,
                )
            )
        }
        return Result.failure(IncidentNotFoundException())
    }

    override suspend fun createOne(incident: Incident): Result<Incident> {
        dao.insertIncident(incident.toEntity())
        return Result.success(incident)
    }


    override fun observeAll(): Flow<List<Incident>> {
        TODO("Not yet implemented")
    }
}

private fun Incident.toEntity(): IncidentEntity {
    return IncidentEntity(
        id = this.id,
        description = this.description,
        solved = this.solved,
        solved_at = this.solved_at,
        latitude = this.latitude,
        longitude = this.longitude,
    )

}
