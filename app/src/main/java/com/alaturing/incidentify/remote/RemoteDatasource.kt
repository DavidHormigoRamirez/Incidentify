package com.alaturing.incidentify.remote

import com.alaturing.incidentify.authentication.model.User
import com.alaturing.incidentify.main.incident.model.Incident

interface RemoteDatasource {

    // Métodos autenticación
    suspend fun login(identifier:String,password:String):Result<User>
    suspend fun register(userName:String,email:String,password:String):Result<User>

    // Métodos incidentes
    suspend fun readAll():Result<List<Incident>>
}