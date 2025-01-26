package com.alaturing.incidentify.main.incident.data.remote

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.alaturing.incidentify.common.exception.IncidentEvidenceUploadException
import com.alaturing.incidentify.common.exception.IncidentNotCreatedException
import com.alaturing.incidentify.common.exception.UserNotAuthorizedException
import com.alaturing.incidentify.common.remote.StrapiApi
import com.alaturing.incidentify.di.NetworkModule
import com.alaturing.incidentify.main.incident.data.remote.model.CreateIncidentPayload
import com.alaturing.incidentify.main.incident.data.remote.model.CreateIncidentPayloadDataWrapper
import com.alaturing.incidentify.main.incident.data.remote.model.toExternal
import com.alaturing.incidentify.main.incident.model.Incident
import com.alaturing.incidentify.main.incident.data.remote.model.toModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import javax.inject.Inject

/**
 * Implementación de [IncidentRemoteDatasource] para gestionar el consumo de una APi en Strapi
 */
class IncidentRemoteDatasourceStrapi @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: StrapiApi
) : IncidentRemoteDatasource {


    /**
     * Lee todos los incidentes remotos
     */
    override suspend fun readAll(): Result<List<Incident>> {
        val response = api.incidentReadAll()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.data.toModel())
        }
        else {
            return when (response.code()) {
                403 -> {
                    // No autorizado
                    Result.failure(UserNotAuthorizedException())
                }

                else -> Result.failure(RuntimeException())
            }
        }
    }



    /**
     * @return Id del incidente creado
     */
    override suspend fun createOne(
        description: String,
        evidence: Uri?,
        latitude: Double?,
        longitude: Double?
    ): Result<Incident> {

        // Convertimos a remoto
        val newIncident = CreateIncidentPayloadDataWrapper(
            CreateIncidentPayload(
                description = description,
                solved = false,
                latitude,
                longitude
            )
        )

        // Creamos el incidente en la API
        val response = api.createIncident(newIncident)
        // si se ha credo subimos el fichero
        if (response.isSuccessful) {
            var uploadedIncident = response.body()!!.data.toExternal()
            // Se ha creado el incidente, si no es nula la imagen la subimos
            evidence?.let { uri ->
                val imageUploaded = uploadIncidentEvidence(uri,response.body()!!.data.id)
                // Si ha subido obtenemos la Uri
                if( imageUploaded.isSuccess) {
                    val uploadedUri = imageUploaded.getOrNull()!!
                    uploadedIncident = uploadedIncident.copy(
                        photoUri = uploadedUri
                    )
                }
            }
            // Se ha creado el incidente
            return Result.success(uploadedIncident)
        } else {
            // No se ha creado
            return Result.failure(IncidentNotCreatedException())
        }
    }

    private suspend fun uploadIncidentEvidence(
        uri: Uri,
        incidentId: Int,
    ): Result<Uri> {
        try {

            // Obtenemos el resolver de MediaStore
            val resolver = context.contentResolver
            // Abrimos el input stream a partir de la URI
            val inputStream = resolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open InputStream from Uri")
            // Obtenemos el tipo del fichero
            val mimeType = resolver.getType(uri) ?: "image/*"
            // Obtenemos el nombre local, esto podiamos cambiarlo a otro patrón
            val fileName = uri.lastPathSegment ?: "evidence.jpg"
            // Convertimos el fichero a cuerpo de la petición
            val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())


            // Construimos la parte de la petición
            val part = MultipartBody.Part.createFormData("files", fileName, requestBody)
            // Map con el resto de parámetros
            val partMap: MutableMap<String, RequestBody> = mutableMapOf()

            // Referencia
            partMap["ref"] = "api::incident.incident".toRequestBody("text/plain".toMediaType())
            // Id del incidente

            partMap["refId"] = incidentId.toString().toRequestBody("text/plain".toMediaType())
            // Campo de la colección
            partMap["field"] = "evidence".toRequestBody("text/plain".toMediaType())

            // Subimos el fichero
            val imageResponse = api.addIncidentEvidence(
                partMap,
                files = part,
            )
            // Si ha ido mal la subida, salimos con error
            if (!imageResponse.isSuccessful) {
                return Result.failure(IncidentEvidenceUploadException())
            }
            else {
                val remoteUri= "${NetworkModule.STRAPI}${imageResponse.body()!!.first().formats.small.url}"
                return Result.success(remoteUri.toUri())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}