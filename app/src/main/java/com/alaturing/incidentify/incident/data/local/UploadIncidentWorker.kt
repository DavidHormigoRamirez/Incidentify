package com.alaturing.incidentify.incident.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alaturing.incidentify.R
import com.alaturing.incidentify.common.exception.NoUnsynchedIncidentsException
import com.alaturing.incidentify.incident.MainActivity.Companion.CHANNEL_ID
import com.alaturing.incidentify.incident.data.remote.IncidentRemoteDatasource
import com.alaturing.incidentify.incident.model.Incident
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [CoroutineWorker] to synchronize local incidents to the remote backend
 */
@HiltWorker
class UploadIncidentWorker @AssistedInject constructor(
        @Assisted val appContext:Context,
        @Assisted val params: WorkerParameters,
        private val localDS: IncidentLocalDatasource,
        private val remoteDS: IncidentRemoteDatasource,
    ): CoroutineWorker(appContext,params) {

    /**
     * Function to upload incidents
     */
    override suspend fun doWork(): Result {

        Log.d("DHR-WORKER","Starting background processing ...")
        // Query the local database for unsynchronized incidents
        val result = localDS.readUnsynchronized()
        // Check if there are no incidents to upload
        return if (result.isFailure) {
            // We verify the exception matches the no incidents
            val exception = result.exceptionOrNull()
            exception?.let {
                if (it is NoUnsynchedIncidentsException)
                    Result.success()
            }
            Result.failure()

        }
        // We upload unsynchronized local incidents
        else {

            val unsyched = result.getOrNull()!!
            var allSynched = true
            // Por cada una, la subimos al remoto
            for (incident in unsyched) {
                val uploaded = remoteDS.createOne(
                    localId =  incident.localId,
                    description = incident.description,
                    evidence = incident.photoUri,
                    latitude = incident.latitude,
                    longitude = incident.longitude
                )
                // Something went wrong during the upload

                if (uploaded.isFailure) {
                    allSynched = false
                }
                // Ha ido bien, la marcamos como sincronizada
                else {
                    withContext(Dispatchers.IO) {
                        localDS.markAsSynchronized(incident)
                    }
                    sendUploadNotification(incident)

                }
            }
            return if (allSynched) {
                Log.d("DHR-WORKER","Successful background processing")
                Result.success()
            }
            else {
                Log.d("DHR-WORKER","Some entries will be retried ...")
                Result.retry()
            }
        }
    }

    /**
     * Function to send a notification
     */
    private fun sendUploadNotification(incident: Incident) {

        // Send the notification only if we have permissions
        with(NotificationManagerCompat.from(appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            // New notification: Incident updated
            val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cloud_sync)
                .setContentTitle(appContext.getString(R.string.notification_title))
                .setContentText(appContext.getString(R.string.updated_notification_content,incident.description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            builder.build()
            // we use the local id of the incident
            notify(incident.localId.toInt(), builder.build())
        }
    }


}