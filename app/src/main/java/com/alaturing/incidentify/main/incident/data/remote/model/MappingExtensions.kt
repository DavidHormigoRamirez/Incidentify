package com.alaturing.incidentify.main.incident.data.remote.model

import com.alaturing.incidentify.di.NetworkModule
import com.alaturing.incidentify.main.incident.model.Incident

fun IncidentResponse.toModel(): Incident {
    return Incident(
        documentId = this.documentId,
        id = this.id,
        description = this.description,
        solved = this.solved ?: false,
        smallPhotoUrl = NetworkModule.STRAPI + this.evidence?.formats?.small?.url,
        thumbnailUrl = NetworkModule.STRAPI + this.evidence?.formats?.thumbnail?.url
    )
}
fun List<IncidentResponse>.toModel():List<Incident> = map(IncidentResponse::toModel)