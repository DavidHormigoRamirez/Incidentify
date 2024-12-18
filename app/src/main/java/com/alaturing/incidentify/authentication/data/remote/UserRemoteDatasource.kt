package com.alaturing.incidentify.authentication.data.remote

import com.alaturing.incidentify.authentication.model.User

interface UserRemoteDatasource {

    // Métodos autenticación
    suspend fun login(identifier:String,password:String):Result<User>
    suspend fun register(userName:String,email:String,password:String):Result<User>

}