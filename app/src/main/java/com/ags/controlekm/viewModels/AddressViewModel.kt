package com.ags.controlekm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.firebaseServices.AddressServices
import com.ags.controlekm.models.Address
import com.ags.controlekm.database.repositorys.AddressRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddressViewModel(application: Application): AndroidViewModel(application) {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("enderecos")

    private val repository: AddressRepository
    lateinit var allAddress: Flow<List<Address>>
    private val enderecoAtendimentoServices: AddressServices

    init {
        val enderecoAtendimentoDao = AppDatabase.getDatabase(application).enderecoAtendimentoDao()
        this.repository = AddressRepository(enderecoAtendimentoDao)

        enderecoAtendimentoServices = AddressServices()

        getAllAdress()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<Address>()
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(Address::class.java)
                    data?.let { dataList.add(it) }
                    viewModelScope.launch(Dispatchers.IO) {
                        data?.let {
                            dataList.add(it)
                            repository.insert(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Trate erros, se necessário
            }
        })

    }

     fun getAllAdress() {
         viewModelScope.launch {
             allAddress = repository.getAllAddress()
         }
    }


    suspend fun insert(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(address)
            enderecoAtendimentoServices.insert(address)
        }
    }

    suspend fun update(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(address)
            enderecoAtendimentoServices.update(address)
        }
    }

    suspend fun delete(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(address)
            enderecoAtendimentoServices.delete(address)
        }
    }

}