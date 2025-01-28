package com.alaturing.incidentify.main.incident.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident")
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true)
    val localId:Int,
    val id: Int?,
    val description: String,
    val solved: Boolean,
    val solved_at:Long?,
    val latitude:Double?,
    val longitude:Double?,
    val photoUri:String?,
    val isSynch: Boolean
    //val smallUrl:String?,
    //val thumbnailUrl:String?,
)
