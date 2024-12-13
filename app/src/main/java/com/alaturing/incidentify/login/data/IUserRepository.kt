package com.alaturing.incidentify.login.data

interface IUserRepository {

    suspend fun login(identifier:String,password:String):User?
    suspend fun register(user:String,email:String,password:String):User?
}