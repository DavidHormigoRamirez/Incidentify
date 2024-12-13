package com.alaturing.incidentify.remote.model

data class AuthRequestBody(
    val identifier:String,
    val password:String,
)
data class AuthResponseUser(
    val id:Int,
    val username:String,
    val email:String,
)
data class AuthResponseBody(
    val jwt:String,
    val user: AuthResponseUser

)

data class RegisterRequestBody(
    val username:String,
    val email:String,
    val password:String,
)

