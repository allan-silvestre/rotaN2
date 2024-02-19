package com.ags.controlekm.database.local.repositories

import com.ags.controlekm.database.local.daos.AddressDao
import com.ags.controlekm.database.models.Address
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepository @Inject constructor(
    private val addressDao: AddressDao
) {
    // Local database
    fun getAllAddress(): Flow<List<Address>> {return addressDao.getAllAddress()}

    suspend fun insert(address: Address) {addressDao.insert(address)}

    suspend fun update(address: Address) {addressDao.update(address)}

    suspend fun delete(address: Address) {addressDao.delete(address)}

    // Online database


}