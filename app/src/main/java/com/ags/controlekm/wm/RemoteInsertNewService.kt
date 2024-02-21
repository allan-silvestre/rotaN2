package com.ags.controlekm.wm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ags.controlekm.database.models.Service
import com.ags.controlekm.database.remote.repositories.FirebaseServiceRepository
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RemoteInsertNewService @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseServiceRepository: FirebaseServiceRepository
): Worker(context, workerParams) {
    override fun doWork(): Result {
        return try {
            val newService = inputData.getString("newService")

            val service = Gson().fromJson(newService, Service::class.java)

            if (newService != null) {
                firebaseServiceRepository.insert(service)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}