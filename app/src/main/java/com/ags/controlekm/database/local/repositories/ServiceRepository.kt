package com.ags.controlekm.database.local.repositories

import com.ags.controlekm.database.local.daos.ServiceDao
import com.ags.controlekm.database.models.Service
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepository @Inject constructor(
    private val serviceDao: ServiceDao,
) {
    fun getAllServices(): Flow<List<Service>> {
        return serviceDao.getAllServices()
    }

    fun getServicesCurrentUser(currentUserId: String): Flow<List<Service>> {
        return serviceDao.getServicesCurrentUser(currentUserId)
    }
    fun getCurrentService(currentUserId: String): Flow<Service>? {
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