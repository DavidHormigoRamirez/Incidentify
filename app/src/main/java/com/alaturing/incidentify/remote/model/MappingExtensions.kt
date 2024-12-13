package com.alaturing.incidentify.remote.model

import com.alaturing.incidentify.authentication.data.model.User

fun AuthResponseBody.toModel(): User {
    return User(
        id = this.user.id,
        userName = this.user.username,
        email = this.user.email,
        password = "", // TODO eliminar
        token = this.jwt
    )
}