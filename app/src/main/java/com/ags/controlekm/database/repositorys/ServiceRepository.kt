package com.ags.controlekm.database.repositorys

import com.ags.controlekm.database.daos.ServiceDao
import com.ags.controlekm.models.Service
import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao) {
    fun getAllServices(): Flow<List<Service>> {
        return serviceDao.getAllServices()
    }

    fun getViagensCurrentUser(tecnicoId: String): Flow<List<Service>> {
        return serviceDao.getViagensCurrentUser(tecnicoId)
    }

    fun getCurrentWeekData(firstDayWeek: Long, lastDayWeek: Long): Flow<List<Service>> {
        return serviceDao.getCurrentWeekData(firstDayWeek, lastDayWeek)
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