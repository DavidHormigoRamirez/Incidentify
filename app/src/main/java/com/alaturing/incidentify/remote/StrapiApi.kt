package com.alaturing.incidentify.remote

import com.alaturing.incidentify.remote.model.AuthRequestBody
import com.alaturing.incidentify.remote.model.AuthResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API del remoto
 */
interface StrapiApi {

    @POST("api/auth/local")
    suspend fun login(@Body body: AuthRequestBody):Response<AuthResponseBody>
}