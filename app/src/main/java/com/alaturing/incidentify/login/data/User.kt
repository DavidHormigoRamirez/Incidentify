package com.alaturing.incidentify.login.data

data class User(
    val id:Int,
    val userName:String,
    val email:String,
    val password:String, // TODO quitar este campo
)
