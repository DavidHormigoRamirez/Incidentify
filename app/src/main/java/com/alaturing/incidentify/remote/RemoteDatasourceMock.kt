package com.alaturing.incidentify.remote

import com.alaturing.incidentify.common.exception.UserNotAuthorizedException
import com.alaturing.incidentify.common.exception.UserNotRegisteredException
import com.alaturing.incidentify.login.data.User
import javax.inject.Inject

/**
 * Clase "fake" que implementa [RemoteDatasource] para simular el login remoto
 */
class RemoteDatasourceMock @Inject constructor():RemoteDatasource {
    private val _users = mutableListOf<User>()
    override suspend fun login(identifier: String, password: String): Result<User> {
        val user = _users.find { u -> (u.userName == identifier || u.email == identifier) && u.password == password }
        user?.let {
            return Result.success(it)
        }
        return Result.failure(UserNotAuthorizedException())
    }

    override suspend fun register(userName: String, email: String, password: String): Result<User> {
        return if (_users.find { u -> (u.userName == userName || u.email == email)}==null) {
            val newUser = User(_users.size+1,userName,email,password)
            _users.add(newUser)
            Result.success(newUser)
        }
        else Result.failure(UserNotRegisteredException())
    }
}