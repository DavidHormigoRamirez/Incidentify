package com.alaturing.incidentify.login.data

import javax.inject.Inject

class UserRepositoryMock @Inject constructor():IUserRepository {
    private val _users = mutableListOf<User>()
    override suspend fun login(identifier: String, password: String):User? =
        _users.find { u -> (u.userName == identifier || u.email == identifier) && u.password == password }


    override suspend fun register(user: String, email: String, password: String) =
        if (_users.find { u -> (u.userName == user || u.email == email)}==null) {
            val newUser = User(_users.size+1,user,email,password)
            _users.add(newUser)
            newUser
        }
        else null
}