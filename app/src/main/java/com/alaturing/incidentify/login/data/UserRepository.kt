package com.alaturing.incidentify.login.data

interface UserRepository {

    suspend fun login(identifier:String,password:String):Result<User>
    suspend fun register(user:String,email:String,password:String):Result<User>
    suspend fun logout()
}