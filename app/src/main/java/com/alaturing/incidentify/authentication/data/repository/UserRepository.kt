package com.alaturing.incidentify.authentication.data.repository

import com.alaturing.incidentify.authentication.model.User

interface UserRepository {

    suspend fun login(identifier:String,password:String):Result<User>
    suspend fun register(user:String,email:String,password:String):Result<User>
    suspend fun logout()
}