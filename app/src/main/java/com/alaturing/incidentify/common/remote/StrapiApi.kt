package com.alaturing.incidentify.common.remote

import com.alaturing.incidentify.authentication.data.remote.model.AuthRequestBody
import com.alaturing.incidentify.authentication.data.remote.model.AuthResponseBody
import com.alaturing.incidentify.main.incident.data.remote.model.IncidentsResponseBody
import com.alaturing.incidentify.authentication.data.remote.model.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API del remoto para Retrofit
 */
interface StrapiApi: StrapiAuthenticationApi,
    StrapiIncidentApi

/**
 * Autenticación
 */
interface StrapiAuthenticationApi {
    @POST("/api/auth/local")
    suspend fun login(@Body body: AuthRequestBody):Response<AuthResponseBody>

    @POST("/api/auth/local/register")
    suspend fun register(@Body body: RegisterRequestBody):Response<AuthResponseBody>
}

/**
 * Incidentes
 */
interface  StrapiIncidentApi {
    // Incidents
    @GET("/api/incidents")
    suspend fun incidentReadAll(@Query("populate") populate:String="evidence"):Response<IncidentsResponseBody>

    @POST("/api/incidents")
    suspend fun createIncident()
}