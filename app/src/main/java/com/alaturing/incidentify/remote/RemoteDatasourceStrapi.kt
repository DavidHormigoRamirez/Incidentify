package com.alaturing.incidentify.remote

import com.alaturing.incidentify.authentication.data.model.User
import com.alaturing.incidentify.common.exception.UserNotAuthorizedException
import com.alaturing.incidentify.remote.model.AuthRequestBody
import com.alaturing.incidentify.remote.model.toModel
import javax.inject.Inject


class RemoteDatasourceStrapi @Inject constructor(
    private val api: StrapiApi
) :RemoteDatasource  {

    override suspend fun login(identifier: String, password: String): Result<User> {

        val response = api.login(AuthRequestBody(identifier, password))
        return if (response.isSuccessful) {
            Result.success(response.body()!!.toModel())
        }
        else {
            Result.failure(UserNotAuthorizedException())
        }
    }

    override suspend fun register(userName: String, email: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }
}