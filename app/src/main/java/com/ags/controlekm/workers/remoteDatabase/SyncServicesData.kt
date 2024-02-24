package com.ags.controlekm.workers.remoteDatabase

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ags.controlekm.database.local.repositories.CurrentUserRepository
import com.ags.controlekm.database.local.repositories.ServiceRepository
import com.ags.controlekm.database.models.CurrentUser
import com.ags.controlekm.database.models.Service
import com.ags.controlekm.database.remote.repositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.remote.repositories.FirebaseServiceRepository
import com.ags.controlekm.ui.views.app.viewModel.AppViewModel
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncServicesData @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val serviceRepository: ServiceRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val firebaseServiceRepository: FirebaseServiceRepository,
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
    private val appViewModel: AppViewModel
): CoroutineWorker(appContext, params){
    override suspend fun doWork(): Result {
        val serviceJson = inputData.getString("service")
        val currentUserJson = inputData.getString("currentUser")

        val service = Gson().fromJson(serviceJson, Service::class.java)
        val currentUser = Gson().fromJson(currentUserJson, CurrentUser::class.java)

        return try {
            if(appViewModel.isNetworkAvailable()) {
                if (service != null && currentUser != null) {
                    // IN LOCAL DATABASE
                    serviceRepository.update(service)
                    currentUserRepository.update(currentUser)
                    // IN REMOTE DATABASE
                    firebaseServiceRepository.update(service)
                    firebaseCurrentUserRepository.update(currentUser)
                    Result.success()
                }else {
                    Result.failure()
                }
            } else {
                if (service != null && currentUser != null) {
                    // IN LOCAL DATABASE
                    serviceRepository.update(service)
                    currentUserRepository.update(currentUser)

                    Result.retry()
                } else{
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            println(e)
            Result.failure()
        }
    }

}