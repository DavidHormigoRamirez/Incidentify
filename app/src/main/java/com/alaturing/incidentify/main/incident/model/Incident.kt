package com.alaturing.incidentify.main.incident.model

import android.net.Uri

/**
 * Entidad modelo que representa un incidente
 */
data class Incident(
    val localId:Int,
    val remoteId:Int?,
    val description:String,
    val solved:Boolean,
    val solved_at:Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val photoUri: Uri?,

    )
