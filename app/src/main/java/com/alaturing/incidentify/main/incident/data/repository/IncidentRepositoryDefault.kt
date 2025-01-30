package com.alaturing.incidentify.main.incident.data.repository


import android.net.Uri
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.alaturing.incidentify.common.exception.IncidentNotCreatedException
import com.alaturing.incidentify.main.incident.data.local.IncidentLocalDatasource
import com.alaturing.incidentify.main.incident.data.local.UploadIncidentWorker
import com.alaturing.incidentify.main.incident.model.Incident
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasource
import kotlinx.coroutines.flow.Flow
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

    override suspend fun readOne(id:Long): Result<Incident> {
        return local.readOne(id)
    }

    override suspend fun createOne(
        description: String,
        latitude: Double?,
        longitude: Double?,
        evidence: Uri?
    ): Result<Incident> {
        // Creamos una nueva incidencia en local
        val localResult = local.createOne(
            description = description,
            latitude = latitude,
            longitude = longitude,
            evidence = evidence
        )
        return if (localResult.isSuccess) {
            val uploadWorkRequest: WorkRequest
            = OneTimeWorkRequestBuilder<UploadIncidentWorker>()
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()
            workManager.enqueue(uploadWorkRequest)
            Result.success(localResult.getOrNull()!!)
        }
        else {
            Result.failure(IncidentNotCreatedException())
        }
    }



    override fun observeAll(): Flow<Result<List<Incident>>> {

        return local.observeAll()

    }
}