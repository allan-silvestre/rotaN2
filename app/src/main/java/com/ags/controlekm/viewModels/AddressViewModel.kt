package com.ags.controlekm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseAddressRepository
import com.ags.controlekm.database.models.database.Address
import com.ags.controlekm.database.repositorys.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository,
    private val firebaseRepository: FirebaseAddressRepository
): ViewModel() {
    var allAddress: Flow<List<Address>> = repository.getAllAddress()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getAllAddress().collect{ addressList ->
                addressList.forEach { address ->
                    repository.insert(address)
                }
            }
        }
    }

    suspend fun insert(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(address)
            firebaseRepository.insert(address)
        }
    }

    suspend fun update(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(address)
            firebaseRepository.update(address)
        }
    }

    suspend fun delete(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(address)
            firebaseRepository.delete(address)
        }
    }

}