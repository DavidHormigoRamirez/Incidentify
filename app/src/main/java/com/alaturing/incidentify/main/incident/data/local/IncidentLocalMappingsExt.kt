package com.alaturing.incidentify.main.incident.data.local

import androidx.core.net.toUri
import com.alaturing.incidentify.main.incident.data.local.database.IncidentEntity
import com.alaturing.incidentify.main.incident.model.Incident

fun Incident.toEntity(): IncidentEntity {
    return IncidentEntity(

        description = this.description,
        solved = this.solved,
        solved_at = this.solved_at,
        latitude = this.latitude,
        longitude = this.longitude,
        photoUri = this.photoUri.toString(),
        isSynch = false,
        localId = this.localId,
        remoteId = this.remoteId
    )

}
fun IncidentEntity.toExternal(): Incident {
    return Incident(
        localId = this.localId,
        remoteId = this.remoteId,
        description = this.description,
        solved = this.solved,
        solved_at = this.solved_at,
        latitude = this.latitude,
        longitude = this.longitude,
        photoUri = this.photoUri?.toUri(),
    )
}
fun List<IncidentEntity>.toExternal():List<Incident> {
    return this.map {
            incident -> incident.toExternal()
    }
}

