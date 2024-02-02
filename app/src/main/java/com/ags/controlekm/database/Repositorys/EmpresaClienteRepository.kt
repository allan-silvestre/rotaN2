package com.ags.controlekm.database.Repositorys

import com.ags.controlekm.database.Daos.EmpresaClienteDao
import com.ags.controlekm.database.Models.EmpresaCliente
import kotlinx.coroutines.flow.Flow

class EmpresaClienteRepository(private val empresaClienteDao: EmpresaClienteDao) {
    val allEmpresaCliente: Flow<List<EmpresaCliente>> = empresaClienteDao.getAllEmpresaCliente()

    suspend fun insert(empresaCliente: EmpresaCliente) {
        empresaClienteDao.insert(empresaCliente)
    }

    suspend fun update(empresaCliente: EmpresaCliente) {
        empresaClienteDao.update(empresaCliente)
    }

    suspend fun delete(empresaCliente: EmpresaCliente) {
        empresaClienteDao.delete(empresaCliente)
    }

}