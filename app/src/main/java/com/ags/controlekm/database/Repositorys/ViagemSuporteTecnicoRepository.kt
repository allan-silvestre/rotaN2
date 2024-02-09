package com.ags.controlekm.database.Repositorys

import com.ags.controlekm.database.Daos.ViagemSuporteTecnicoDao
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import kotlinx.coroutines.flow.Flow

class ViagemSuporteTecnicoRepository(private val viagemSuporteTecnicoDao: ViagemSuporteTecnicoDao) {
    fun getAllServices(): Flow<List<ViagemSuporteTecnico>> {
        return viagemSuporteTecnicoDao.getAllServices()
    }

    fun getViagensCurrentUser(tecnicoId: String): Flow<List<ViagemSuporteTecnico>> {
        return viagemSuporteTecnicoDao.getViagensCurrentUser(tecnicoId)
    }

    fun getCurrentWeekData(firstDayWeek: Long, lastDayWeek: Long): Flow<List<ViagemSuporteTecnico>> {
        return viagemSuporteTecnicoDao.getCurrentWeekData(firstDayWeek, lastDayWeek)
    }

    suspend fun insert(viagemSuporteTecnico: ViagemSuporteTecnico) {
        viagemSuporteTecnicoDao.insert(viagemSuporteTecnico)
    }

    suspend fun update(viagemSuporteTecnico: ViagemSuporteTecnico) {
        viagemSuporteTecnicoDao.update(viagemSuporteTecnico)
    }

    suspend fun delete(viagemSuporteTecnico: ViagemSuporteTecnico) {
        viagemSuporteTecnicoDao.delete(viagemSuporteTecnico)
    }
}