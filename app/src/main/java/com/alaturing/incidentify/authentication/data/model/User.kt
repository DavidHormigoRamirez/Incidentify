package com.alaturing.incidentify.authentication.data.model

data class User(
    val id:Int,
    val userName:String,
    val email:String,
    val password:String, // TODO quitar este campo
    var token: String?
)
