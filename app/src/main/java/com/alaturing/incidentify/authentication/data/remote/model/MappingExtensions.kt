package com.alaturing.incidentify.authentication.data.remote.model

import com.alaturing.incidentify.authentication.model.User

fun AuthResponseBody.toModel(): User {
    return User(
        id = this.user.id,
        userName = this.user.username,
        email = this.user.email,
        token = this.jwt
    )
}


