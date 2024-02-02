package com.ags.controlekm.database.Repositorys

import com.ags.controlekm.database.Daos.UserDao
import com.ags.controlekm.database.Daos.ViagemSuporteTecnicoDao
import com.ags.controlekm.database.Models.User
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import kotlinx.coroutines.flow.Flow

class ViagemSuporteTecnicoRepository(private val viagemSuporteTecnicoDao: ViagemSuporteTecnicoDao) {
    val allViagemSuporteTecnico: Flow<List<ViagemSuporteTecnico>> = viagemSuporteTecnicoDao.getAllViagemSuporteTecnico()

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