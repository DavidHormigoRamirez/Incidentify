package com.alaturing.incidentify.authentication.data.local

import com.alaturing.incidentify.authentication.data.model.User

interface UserLocalDatasource {
    suspend fun saveUser(user: User)
    suspend fun retrieveUser(): User?
    suspend fun clearUser()
}