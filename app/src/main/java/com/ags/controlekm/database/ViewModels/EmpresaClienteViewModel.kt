package com.ags.controlekm.database.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.Models.EmpresaCliente
import com.ags.controlekm.database.Repositorys.EmpresaClienteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EmpresaClienteViewModel(application: Application) : AndroidViewModel(application){
    private val repository: EmpresaClienteRepository
    val allEmpresaCliente: Flow<List<EmpresaCliente>>

    init{
        val empresaClienteDao = AppDatabase.getDatabase(application).empresaClienteDao()
        this.repository = EmpresaClienteRepository(empresaClienteDao)
        allEmpresaCliente = repository.allEmpresaCliente
    }

    fun insert(empresaCliente: EmpresaCliente) {
        viewModelScope.launch {
            repository.insert(empresaCliente)
        }
    }

    fun update(empresaCliente: EmpresaCliente) {
        viewModelScope.launch {
            repository.update(empresaCliente)
        }
    }

    fun delete(empresaCliente: EmpresaCliente) {
        viewModelScope.launch {
            repository.delete(empresaCliente)
        }
    }


}