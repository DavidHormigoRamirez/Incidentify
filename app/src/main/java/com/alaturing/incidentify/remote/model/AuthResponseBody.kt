package com.alaturing.incidentify.remote.model

data class AuthResponseUser(
    val id:Int,
    val username:String,
    val email:String,
)
data class AuthResponseBody(
    val jwt:String,
    val user: AuthResponseUser

)
