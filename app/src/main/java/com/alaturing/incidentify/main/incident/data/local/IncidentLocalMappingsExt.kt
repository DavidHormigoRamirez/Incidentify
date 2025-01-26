package com.alaturing.incidentify.main.incident.data.local

import androidx.core.net.toUri
import com.alaturing.incidentify.main.incident.data.local.database.IncidentEntity
import com.alaturing.incidentify.main.incident.model.Incident

fun Incident.toEntity(): IncidentEntity {
    return IncidentEntity(
        id = this.id,
        description = this.description,
        solved = this.solved,
        solved_at = this.solved_at,
        latitude = this.latitude,
        longitude = this.longitude,
        photoUri = this.photoUri.toString()
        //smallUrl = this.smallPhotoUrl,
        //thumbnailUrl = null
    )

}
fun IncidentEntity.toExternal(): Incident {
    return Incident(
        id = this.id,
        description = this.description,
        solved = this.solved,
        solved_at = this.solved_at,
        latitude = this.latitude,
        longitude = this.longitude,
        photoUri = this.photoUri?.toUri(),
        //smallPhotoUrl = this.smallUrl,
        //thumbnailUrl = this.thumbnailUrl,
    )
}
fun List<IncidentEntity>.toExternal():List<Incident> {
    return this.map {
            incident -> incident.toExternal()
    }
}

