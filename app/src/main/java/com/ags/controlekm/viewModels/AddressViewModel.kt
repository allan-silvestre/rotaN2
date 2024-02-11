package com.ags.controlekm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.MyApp
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.firebaseServices.AddressServices
import com.ags.controlekm.models.Address
import com.ags.controlekm.database.repositorys.AddressRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
): ViewModel() {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("address")

    //private val repository: AddressRepository
    lateinit var allAddress: Flow<List<Address>>
    private val enderecoAtendimentoServices: AddressServices = AddressServices()

    init {
        allAddress = addressRepository.getAllAddress()
        //val enderecoAtendimentoDao = AppDatabase.getDatabase(application).enderecoAtendimentoDao()
        //this.repository = AddressRepository(enderecoAtendimentoDao)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<Address>()
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(Address::class.java)
                    data?.let { dataList.add(it) }
                    viewModelScope.launch(Dispatchers.IO) {
                        data?.let {
                            dataList.add(it)
                            addressRepository.insert(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

     fun getAllAdress() {
         viewModelScope.launch {
             allAddress = addressRepository.getAllAddress()
         }
    }


    suspend fun insert(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.insert(address)
            enderecoAtendimentoServices.insert(address)
        }
    }

    suspend fun update(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.update(address)
            enderecoAtendimentoServices.update(address)
        }
    }

    suspend fun delete(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.delete(address)
            enderecoAtendimentoServices.delete(address)
        }
    }

}