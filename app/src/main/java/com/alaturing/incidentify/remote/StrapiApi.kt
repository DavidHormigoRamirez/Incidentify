package com.alaturing.incidentify.remote

import com.alaturing.incidentify.remote.model.AuthRequestBody
import com.alaturing.incidentify.remote.model.AuthResponseBody
import com.alaturing.incidentify.remote.model.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API del remoto para Retrofit
 */
interface StrapiApi {

    @POST("api/auth/local")
    suspend fun login(@Body body: AuthRequestBody):Response<AuthResponseBody>

    @POST("api/auth/local/register")
    suspend fun register(@Body body: RegisterRequestBody):Response<AuthResponseBody>
}