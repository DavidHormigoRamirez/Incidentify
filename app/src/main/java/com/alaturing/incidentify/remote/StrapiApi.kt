package com.alaturing.incidentify.remote

import com.alaturing.incidentify.remote.model.AuthRequestBody
import com.alaturing.incidentify.remote.model.AuthResponseBody
import com.alaturing.incidentify.remote.model.IncidentsResponseBody
import com.alaturing.incidentify.remote.model.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API del remoto para Retrofit
 */
interface StrapiApi: StrapiAuthenticationApi,
                     StrapiIncidentApi

/**
 * Autenticación
 */
interface StrapiAuthenticationApi {
    @POST("auth/local")
    suspend fun login(@Body body: AuthRequestBody):Response<AuthResponseBody>

    @POST("auth/local/register")
    suspend fun register(@Body body: RegisterRequestBody):Response<AuthResponseBody>
}

/**
 * Incidentes
 */
interface  StrapiIncidentApi {
    // Incidents
    @GET("incidents")
    suspend fun incidentReadAll():Response<IncidentsResponseBody>

    @POST("incidents")
    suspend fun createIncident()
}