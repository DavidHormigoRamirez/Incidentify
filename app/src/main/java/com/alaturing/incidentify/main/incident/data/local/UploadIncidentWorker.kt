package com.alaturing.incidentify.main.incident.data.local

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadIncidentWorker
    @AssistedInject constructor(

        @Assisted appContext:Context,
        @Assisted params: WorkerParameters,
        val localDS: IncidentLocalDatasource,
        val remoteDS: IncidentRemoteDatasource,

    ): CoroutineWorker(appContext,params) {


    override suspend fun doWork(): Result {
        Log.d("DHR-WORKER","Starting background processing ...")
        val result = localDS.readUnsynched()
        // No hay incidentes
        return if (result.isFailure) {
            // TODO Comprobar excepcion
            //unsychedIncidents.exceptionOrNull()
            Result.success()

        }
        // Hay incidentes que subir
        else {
            // Obtenemos las incidencais no sincronizadas
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
                // Si alguna va mal, consideramos la subida incorrecta
                if (uploaded.isFailure) {
                    allSynched = false
                }
                // Ha ido bien, la marcamos como sincronizada
                else {
                    localDS.markAsSynched(incident)
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


}