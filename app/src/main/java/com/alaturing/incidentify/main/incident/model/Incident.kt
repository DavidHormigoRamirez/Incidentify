package com.alaturing.incidentify.main.incident.model

/**
 * Entidad modelo que representa un incidente
 */
data class Incident(
    //val documentId:String,
    val id:Int,
    val description:String,
    val solved:Boolean,
    val solved_at:Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val smallPhotoUrl:String?=null,
    val thumbnailUrl:String?=null,

)
