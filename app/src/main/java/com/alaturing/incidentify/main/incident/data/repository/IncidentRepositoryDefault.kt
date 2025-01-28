package com.alaturing.incidentify.main.incident.data.repository

import android.content.Context
import android.net.Uri
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.alaturing.incidentify.common.exception.IncidentNotCreatedException
import com.alaturing.incidentify.main.incident.data.local.IncidentLocalDatasource
import com.alaturing.incidentify.main.incident.data.local.UploadIncidentWorker
import com.alaturing.incidentify.main.incident.model.Incident
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class IncidentRepositoryDefault @Inject constructor(
    private val remote: IncidentRemoteDatasource,
    private val local: IncidentLocalDatasource,
    //@ApplicationContext private val context: Context
    private val workManager: WorkManager
):IncidentRepository {
    override suspend fun readAll(): Result<List<Incident>> {
        val result = remote.readAll()

        return result
    }

    override suspend fun readOne(id:Int): Result<Incident> {
        return local.readOne(id)
    }

    override suspend fun createOne(
        description: String,
        latitude: Double?,
        longitude: Double?,
        evidence: Uri?
    ): Result<Incident> {
        val localResult = local.createOne(
            Incident(
                id = 0,
                description = description,
                solved = false,
                solved_at = null,
                latitude = latitude,
                longitude = longitude,
                photoUri = evidence
            )

        )
        return if (localResult.isSuccess) {
            val uploadWorkRequest: WorkRequest
            = OneTimeWorkRequestBuilder<UploadIncidentWorker>()
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()
            //WorkManager.getInstance(context).enqueue(uploadWorkRequest)
            workManager.enqueue(uploadWorkRequest)
            Result.success(localResult.getOrNull()!!)
        }
        else {
            Result.failure(IncidentNotCreatedException())
        }
    }

    /**
     * VERSION REMOTO PRIMERO
    override suspend fun createOne(
        description: String,
        latitude: Double?,
        longitude: Double?,
        evidence: Uri?
    ): Result<Incident> {
        // Subimos el incidente al backend
        val result = remote.createOne(description,evidence,latitude,longitude)
        if (result.isSuccess) {
            val incident = result.getOrNull()
            incident?.let {
                local.createOne(it)
            }
        }
        return result
    } */

    override fun observeAll(): Flow<Result<List<Incident>>> {

        return local.observeAll()


        /**val incidents = flow<Result<List<Incident>>> {
            val result = remote .readAll()
            emit(result)
        }
        return incidents*/
    }
}