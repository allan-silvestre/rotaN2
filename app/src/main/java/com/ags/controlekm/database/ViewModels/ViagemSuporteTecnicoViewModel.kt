package com.ags.controlekm.database.ViewModels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.FirebaseServices.ViagemSuporteTecnicoServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.Repositorys.ViagemSuporteTecnicoRepository
import com.ags.controlekm.functions.navigateSingleTopTo
import com.ags.controlekm.functions.reiniciarTela
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ViagemSuporteTecnicoViewModel(application: Application
) : AndroidViewModel(application) {
    var context: Application = getApplication()

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("atendimentos")

    private val repository: ViagemSuporteTecnicoRepository
    val allViagemSuporteTecnico: Flow<List<ViagemSuporteTecnico>>
    private val viagemSuporteTecnicoServices: ViagemSuporteTecnicoServices

    val userLoggedData = mutableStateOf<CurrentUser?>(null)
    val currentUserViewModel: CurrentUserViewModel = CurrentUserViewModel(context)
    val currentUserServices: CurrentUserServices = CurrentUserServices(userLoggedData.value?.id.toString())

    init {
        viewModelScope.launch {
            currentUserViewModel.currentUserData.collect { data ->
                userLoggedData.value = data
            }
        }

        val viagemSuporteTecnicoDao = AppDatabase.getDatabase(application).viagemSuporteTecnicoDao()
        this.repository = ViagemSuporteTecnicoRepository(viagemSuporteTecnicoDao)
        allViagemSuporteTecnico = repository.allViagemSuporteTecnico
        viagemSuporteTecnicoServices = ViagemSuporteTecnicoServices()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<ViagemSuporteTecnico>()
                //val dataList = mutableStateOf<List<EnderecoAtendimento>>(emptyList())
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(ViagemSuporteTecnico::class.java)
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

    suspend fun insert(viagemSuporteTecnico: ViagemSuporteTecnico) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(viagemSuporteTecnico)
            viagemSuporteTecnicoServices.insert(viagemSuporteTecnico)
        }
    }

    suspend fun update(viagemSuporteTecnico: ViagemSuporteTecnico) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(viagemSuporteTecnico)
            viagemSuporteTecnicoServices.update(viagemSuporteTecnico)
        }
    }

    suspend fun delete(viagemSuporteTecnico: ViagemSuporteTecnico) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(viagemSuporteTecnico)
            viagemSuporteTecnicoServices.delete(viagemSuporteTecnico)
        }
    }

    // FUNÇÕES DE LÓGICA DE NEGOCIO
    fun iniciarViagem(
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
        ultimoKm: String,
        data: String,
        hora: String,
        title: String,
        navController: NavHostController
    ) {
        val atendimento = mutableStateOf(ViagemSuporteTecnico())

        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (localSaida.isEmpty() || localAtendimento.isEmpty() || kmSaida.isEmpty()) {
            Toast.makeText(context, "Preencha todos os campos para continuar", Toast.LENGTH_SHORT)
                .show()
            // VERIFICA SE O LOCAL DE SAIDA É IGUAL AO LOCAL DE ATENDIMENTO
        } else if (localSaida.equals(localAtendimento)) {
            Toast.makeText(
                context,
                "O local de saida não pode ser o mesmo local do atendimento",
                Toast.LENGTH_SHORT
            ).show()
            // VERIFICA SE O KM DE SAIDA É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmSaida <= ultimoKm) {
            Toast.makeText(context, "KM inferior ao último informado", Toast.LENGTH_SHORT).show()
            // INICIA A VIAGEM
        } else {
            atendimento.value.dataSaida = data
            atendimento.value.horaSaida = hora
            atendimento.value.localSaida = localSaida
            atendimento.value.localAtendimento = localAtendimento
            atendimento.value.kmSaida = kmSaida
            atendimento.value.tecnicoId = userLoggedData.value?.id
            atendimento.value.tecnicoNome = "${userLoggedData.value?.nome} ${userLoggedData.value?.sobrenome}"
            atendimento.value.imgPerfil = userLoggedData.value?.imagem
            atendimento.value.statusService = "Em rota"
            viewModelScope.launch(Dispatchers.IO) {
                insert(atendimento.value)
                currentUserViewModel.update(
                    CurrentUser(
                        id = userLoggedData.value?.id.toString(),
                        ultimoKm = kmSaida,
                    )
                )
                currentUserServices.addUltimoKm(kmSaida)
            }
        }
        reiniciarTela(title, navController)
    }
}