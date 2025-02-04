package com.alaturing.incidentify.incident.data.remote.model

import androidx.core.net.toUri
import com.alaturing.incidentify.di.NetworkModule
import com.alaturing.incidentify.incident.model.Incident
/**
fun IncidentResponse.toModel(): Incident {
    return Incident(
        remoteId = this.id,
        description = this.attributes.description,
        solved = this.attributes.solved ?: false,
        photoUri = "${NetworkModule.STRAPI}${this.attributes.evidence?.formats?.small?.url}".toUri(),
        localId = this.
        solved_at = TODO(),
        latitude = TODO(),
        longitude = TODO()
        //smallPhotoUrl = NetworkModule.STRAPI + this.attributes.evidence?.formats?.small?.url,
        //thumbnailUrl = NetworkModule.STRAPI + this.attributes.evidence?.formats?.thumbnail?.url
    )
}
fun List<IncidentResponse>.toModel():List<Incident> = map(IncidentResponse::toModel)*/

fun Incident.toRemoteModel():CreateIncidentPayloadDataWrapper {
    return CreateIncidentPayloadDataWrapper(CreateIncidentPayload(
        localId = this.localId,
        description = this.description,
        solved = this.solved
    ))
}