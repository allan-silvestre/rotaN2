package com.ags.controlekm.database.repositorys

import com.ags.controlekm.database.daos.AddressDao
import com.ags.controlekm.models.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val addressDao: AddressDao
) {
    fun getAllAddress(): Flow<List<Address>> {return addressDao.getAllAddress()}

    suspend fun insert(address: Address) {addressDao.insert(address)}

    suspend fun update(address: Address) {addressDao.update(address)}

    suspend fun delete(address: Address) {addressDao.delete(address)}

}