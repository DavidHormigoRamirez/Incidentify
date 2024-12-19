package com.alaturing.incidentify.authentication.data.remote

import com.alaturing.incidentify.authentication.model.User
import com.alaturing.incidentify.common.exception.UserNotAuthorizedException
import com.alaturing.incidentify.common.exception.UserNotRegisteredException
import com.alaturing.incidentify.authentication.data.remote.model.AuthRequestBody
import com.alaturing.incidentify.authentication.data.remote.model.RegisterRequestBody
import com.alaturing.incidentify.authentication.data.remote.model.toModel
import com.alaturing.incidentify.common.remote.StrapiApi
import javax.inject.Inject

/**
 * Implementación de [UserRemoteDatasource] para gestionar el consumo de una APi en Strapi
 */
class UserRemoteDatasourceStrapi @Inject constructor(
    private val api: StrapiApi
) : UserRemoteDatasource {

    /**
     * @param identifier
     * @param password
     * @return [Result]
     */
    override suspend fun login(identifier: String, password: String): Result<User> {

        //
        val response = api.login(AuthRequestBody(identifier, password))
        return if (response.isSuccessful) {
            Result.success(response.body()!!.toModel())
        }
        else {
            Result.failure(UserNotAuthorizedException())
        }
    }

    override suspend fun register(userName: String, email: String, password: String): Result<User> {
        // Usuario que vamos a registrar
        val registerBody = RegisterRequestBody(
            username = userName,
            email = email,
            password = password
        )
        // Lo registramos en la API
        val response = api.register(registerBody)
        // Si ha ido bien lo convertimos al modelo externo
        return if (response.isSuccessful) {
            Result.success(response.body()!!.toModel())
        }
        else {
            Result.failure(UserNotRegisteredException())
        }
    }


}