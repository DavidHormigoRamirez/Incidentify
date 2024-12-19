package com.alaturing.incidentify.authentication.data.remote.model

import com.alaturing.incidentify.authentication.model.User
import com.alaturing.incidentify.di.NetworkModule
import com.alaturing.incidentify.main.incident.data.remote.model.IncidentResponse
import com.alaturing.incidentify.main.incident.model.Incident

fun AuthResponseBody.toModel(): User {
    return User(
        id = this.user.id,
        userName = this.user.username,
        email = this.user.email,
        token = this.jwt
    )
}


