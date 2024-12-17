package com.alaturing.incidentify.main.incident.model

data class Incident(
    val documentId:String,
    val id:Int,
    val description:String,
    val solved:Boolean

)
