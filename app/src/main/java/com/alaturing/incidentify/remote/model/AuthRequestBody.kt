package com.alaturing.incidentify.remote.model

data class AuthRequestBody(
    val identifier:String,
    val password:String,
)
