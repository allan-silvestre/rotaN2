package com.ags.controlekm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.models.database.Company
import com.ags.controlekm.database.repositorys.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel(){

    val allCompany: Flow<List<Company>> = repository.allCompany

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