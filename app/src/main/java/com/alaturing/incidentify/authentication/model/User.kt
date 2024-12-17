package com.alaturing.incidentify.authentication.model

data class User(
    val id:Int,
    val userName:String,
    val email:String,
    //val password:String, // TODO quitar este campo
    var token: String?,
)
{
    val isLoggedIn:Boolean
        get() = token != null
}
