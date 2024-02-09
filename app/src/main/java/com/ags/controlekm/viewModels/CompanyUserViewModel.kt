package com.ags.controlekm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.models.Company
import com.ags.controlekm.database.repositorys.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CompanyUserViewModel(application: Application) : AndroidViewModel(application){
    private val repository: CompanyRepository
    val allCompany: Flow<List<Company>>

    init{
        val empresaClienteDao = AppDatabase.getDatabase(application).empresaClienteDao()
        this.repository = CompanyRepository(empresaClienteDao)
        allCompany = repository.allCompany
    }

    fun insert(company: Company) {
        viewModelScope.launch {
            repository.insert(company)
        }
    }

    fun update(company: Company) {
        viewModelScope.launch {
            repository.update(company)
        }
    }

    fun delete(company: Company) {
        viewModelScope.launch {
            repository.delete(company)
        }
    }


}