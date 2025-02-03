package com.alaturing.incidentify.incident.data.remote.model

import androidx.core.net.toUri
import com.alaturing.incidentify.incident.model.Incident

data class IncidentsResponseBody(
    val data: List<IncidentResponse>
)
data class IncidentResponseBody(
    val data: IncidentResponse
)
data class IncidentResponse(
    val id:Long,
    //val documentId:String,
    val attributes: IncidentAttributes
)

fun IncidentResponse.toExternal(): Incident {

    return Incident(
        remoteId = this.id,
        description = this.attributes.description,
        solved = this.attributes.solved,
        photoUri = this.attributes.evidence?.formats?.small?.url?.toUri(),
        //smallPhotoUrl = this.attributes.evidence?.formats?.small?.url,
        //thumbnailUrl = this.attributes.evidence?.formats?.thumbnail?.url,
        latitude = this.attributes.latitude,
        longitude = this.attributes.longitude,
        localId = this.attributes.localId,
        solved_at = this.attributes.solved_at?.toLong()
    )
}

data class IncidentAttributes(
    val description: String,
    val solved:Boolean,
    val solved_at:String?,
    val evidence: Media?,
    val latitude: Double?,
    val longitude: Double?,
    val localId:Long
)

data class Media(
    val documentId: String,
    val formats: MediaFormats
)

data class MediaFormats(
    val small: ImageAttributes,
    val thumbnail: ImageAttributes,
)

data class ImageAttributes(
    val url: String
)

// Modelos para crear
data class CreateIncidentPayloadDataWrapper(
    val data: CreateIncidentPayload
)
// Cuerpo
data class CreateIncidentPayload(
    val localId: Long,
    val description: String,
    val solved: Boolean,
    val latitude: Double?=null,
    val longitude: Double?=null,
)

data class CreatedMediaItemResponse(
    val id:Int,
    val documentId: String,
    val name:String,
    val formats: MediaFormats
)
