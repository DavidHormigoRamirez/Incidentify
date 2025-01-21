package com.alaturing.incidentify.main.incident.data.local

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
        solved_at = this.solved_at
    )

}
