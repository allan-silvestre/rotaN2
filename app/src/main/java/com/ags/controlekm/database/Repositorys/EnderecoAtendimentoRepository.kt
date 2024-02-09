package com.ags.controlekm.database.Repositorys

import com.ags.controlekm.database.Daos.EnderecoAtendimentoDao
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import kotlinx.coroutines.flow.Flow

class EnderecoAtendimentoRepository(private val enderecoAtendimentoDao: EnderecoAtendimentoDao) {
    fun getAllAddress(): Flow<List<EnderecoAtendimento>> {
        return enderecoAtendimentoDao.getAllAddress()
    }

    suspend fun insert(enderecoAtendimento: EnderecoAtendimento) {
        enderecoAtendimentoDao.insert(enderecoAtendimento)
    }

    suspend fun update(enderecoAtendimento: EnderecoAtendimento) {
        enderecoAtendimentoDao.update(enderecoAtendimento)
    }

    suspend fun delete(enderecoAtendimento: EnderecoAtendimento) {
        enderecoAtendimentoDao.delete(enderecoAtendimento)
    }

}