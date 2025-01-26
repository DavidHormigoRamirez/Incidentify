package com.alaturing.incidentify.main.incident.data.remote.model

import androidx.core.net.toUri
import com.alaturing.incidentify.di.NetworkModule
import com.alaturing.incidentify.main.incident.model.Incident

fun IncidentResponse.toModel(): Incident {
    return Incident(
        id = this.id,
        description = this.attributes.description,
        solved = this.attributes.solved ?: false,
        photoUri = "${NetworkModule.STRAPI}${this.attributes.evidence?.formats?.small?.url}".toUri()
        //smallPhotoUrl = NetworkModule.STRAPI + this.attributes.evidence?.formats?.small?.url,
        //thumbnailUrl = NetworkModule.STRAPI + this.attributes.evidence?.formats?.thumbnail?.url
    )
}
fun List<IncidentResponse>.toModel():List<Incident> = map(IncidentResponse::toModel)

fun Incident.toRemoteModel():CreateIncidentPayloadDataWrapper {
    return CreateIncidentPayloadDataWrapper(CreateIncidentPayload(
        description = this.description,
        solved = this.solved
    ))
}