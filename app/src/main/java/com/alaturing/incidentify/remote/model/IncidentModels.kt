package com.alaturing.incidentify.remote.model

data class IncidentsResponseBody(
    val data: List<IncidentResponse>
)

data class IncidentResponse(
    val id:Int,
    val documentId:String,
    val description:String,
    val solved:Boolean?,
    val solved_at:String?,
)