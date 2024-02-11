package com.ags.controlekm.database.repositorys

import com.ags.controlekm.database.daos.CompanyDao
import com.ags.controlekm.models.Company
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val companyDao: CompanyDao
) {
    val allCompany: Flow<List<Company>> = companyDao.getAllEmpresaCliente()
    suspend fun insert(company: Company) {
        companyDao.insert(company)
    }
    suspend fun update(company: Company) {
        companyDao.update(company)
    }
    suspend fun delete(company: Company) {
        companyDao.delete(company)
    }
}