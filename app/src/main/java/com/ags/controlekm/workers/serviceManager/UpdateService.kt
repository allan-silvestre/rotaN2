package com.ags.controlekm.workers.serviceManager

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltWorker
class UpdateService @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val firebaseServiceRepository: FirebaseServiceRepository,
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val serviceJson = inputData.getString("service")
        val currentUserJson = inputData.getString("currentUser")

        val service = Gson().fromJson(serviceJson, Service::class.java)
        val currentUser = Gson().fromJson(currentUserJson, CurrentUser::class.java)

        return try {
            if (isNetworkAvailable()) {
                if (service != null && currentUser != null) {
                    firebaseServiceRepository.insert(service)
                    firebaseCurrentUserRepository.update(currentUser)
                    Result.success()
                } else {
                    Result.failure()
                }
            } else {
                if (service != null && currentUser != null) {
                    Result.retry()
                } else {
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            println(e)
            Result.failure()
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities)

        return when {
            activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
            activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
            activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
            else -> false
        }
    }

}