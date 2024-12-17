package com.alaturing.incidentify.remote.model

import com.alaturing.incidentify.authentication.model.User
import com.alaturing.incidentify.main.incident.model.Incident

fun AuthResponseBody.toModel(): User {
    return User(
        id = this.user.id,
        userName = this.user.username,
        email = this.user.email,
        token = this.jwt
    )
}

fun IncidentResponse.toModel():Incident {
    return Incident(
        documentId = this.documentId,
        id = this.id,
        description = this.description,
        solved = this.solved ?: false
    )
}
fun List<IncidentResponse>.toModel():List<Incident> = map(IncidentResponse::toModel)
