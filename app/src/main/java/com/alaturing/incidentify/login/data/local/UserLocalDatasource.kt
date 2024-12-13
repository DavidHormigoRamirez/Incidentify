package com.alaturing.incidentify.login.data.local

import com.alaturing.incidentify.login.data.User

interface UserLocalDatasource {
    suspend fun saveUser(user: User)
    suspend fun retrieveUser():User?
    suspend fun clearUser()
}