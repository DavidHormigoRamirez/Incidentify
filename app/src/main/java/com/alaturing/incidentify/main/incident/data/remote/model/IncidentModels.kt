package com.alaturing.incidentify.main.incident.data.remote.model

data class IncidentsResponseBody(
    val data: List<IncidentResponse>
)

data class IncidentResponse(
    val id:Int,
    val documentId:String,
    val description:String,
    val solved:Boolean?,
    val solved_at:String?,
    val evidence: Media?,
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
