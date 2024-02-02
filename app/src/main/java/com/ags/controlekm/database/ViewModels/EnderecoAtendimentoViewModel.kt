package com.ags.controlekm.database.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.FirebaseServices.EnderecoAtendimentoServices
import com.ags.controlekm.database.FirebaseServices.ViagemSuporteTecnicoServices
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.Repositorys.EnderecoAtendimentoRepository
import com.ags.controlekm.database.Repositorys.ViagemSuporteTecnicoRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EnderecoAtendimentoViewModel(application: Application): AndroidViewModel(application) {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("enderecos")

    private val repository: EnderecoAtendimentoRepository
    val allEnderecoAtendimento: Flow<List<EnderecoAtendimento>>
    private val enderecoAtendimentoServices: EnderecoAtendimentoServices

    init {
        val enderecoAtendimentoDao = AppDatabase.getDatabase(application).enderecoAtendimentoDao()
        this.repository = EnderecoAtendimentoRepository(enderecoAtendimentoDao)
        allEnderecoAtendimento = repository.allEnderecoAtendimento
        enderecoAtendimentoServices = EnderecoAtendimentoServices()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<EnderecoAtendimento>()
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(EnderecoAtendimento::class.java)
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

    suspend fun insert(enderecoAtendimento: EnderecoAtendimento) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(enderecoAtendimento)
            enderecoAtendimentoServices.insert(enderecoAtendimento)
        }
    }

    suspend fun update(enderecoAtendimento: EnderecoAtendimento) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(enderecoAtendimento)
            enderecoAtendimentoServices.update(enderecoAtendimento)
        }
    }

    suspend fun delete(enderecoAtendimento: EnderecoAtendimento) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(enderecoAtendimento)
            enderecoAtendimentoServices.delete(enderecoAtendimento)
        }
    }

}