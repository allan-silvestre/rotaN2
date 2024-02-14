package com.ags.controlekm.database.repositorys

import com.ags.controlekm.database.daos.ServiceDao
import com.ags.controlekm.models.database.Service
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepository @Inject constructor(
    private val serviceDao: ServiceDao,
) {
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    fun getAllServices(): Flow<List<Service>> {
        return serviceDao.getAllServices()
    }

    fun getServicesCurrentUser(): Flow<List<Service>> {
        return serviceDao.getServicesCurrentUser(currentUserId)
    }
    fun getcurrentService(): Flow<Service>? {
        return serviceDao.getCurrentService(
            currentUserId,
            "Em rota",
            "Em andamento",
            "Em rota, retornando")
    }

    suspend fun insert(service: Service) {
        serviceDao.insert(service)
    }

    suspend fun update(service: Service) {
        serviceDao.update(service)
    }

    suspend fun delete(service: Service) {
        serviceDao.delete(service)
    }
}