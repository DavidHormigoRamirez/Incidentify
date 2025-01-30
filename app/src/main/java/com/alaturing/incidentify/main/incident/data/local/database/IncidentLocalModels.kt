package com.alaturing.incidentify.main.incident.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "incident")
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true)
    val localId:Long,
    val remoteId: Long?,
    val description: String,
    @ColumnInfo(defaultValue = "0")
    val solved: Boolean,
    val solved_at:Long?,
    val latitude:Double?,
    val longitude:Double?,
    val photoUri:String?,
    @ColumnInfo(defaultValue = "0")
    val isSynch: Boolean
)

data class NewIncidentEntity(
    val description: String,
    val latitude: Double?,
    val longitude: Double?,
    val photoUri: String?
)
