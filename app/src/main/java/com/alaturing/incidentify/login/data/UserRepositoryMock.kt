package com.alaturing.incidentify.login.data

import com.alaturing.incidentify.remote.RemoteDatasource
import javax.inject.Inject

class UserRepositoryMock @Inject constructor(
    private val remote:RemoteDatasource
):UserRepository {
    override suspend fun login(identifier: String, password: String) =
        remote.login(identifier,password)

    override suspend fun register(user: String, email: String, password: String):Result<User> {
        return remote.register(user,email,password)
    }

}