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
import com.ags.controlekm.functions.reiniciarTela
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ViagemSuporteTecnicoViewModel(application: Application) : AndroidViewModel(application) {
    var context: Application = getApplication()

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("atendimentos")

    private val repository: ViagemSuporteTecnicoRepository
    val allViagemSuporteTecnico: Flow<List<ViagemSuporteTecnico>>
    private val viagemSuporteTecnicoServices: ViagemSuporteTecnicoServices

    init {
        val viagemSuporteTecnicoDao = AppDatabase.getDatabase(application).viagemSuporteTecnicoDao()
        this.repository = ViagemSuporteTecnicoRepository(viagemSuporteTecnicoDao)
        allViagemSuporteTecnico = repository.allViagemSuporteTecnico
        viagemSuporteTecnicoServices = ViagemSuporteTecnicoServices()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<ViagemSuporteTecnico>()
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
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser?,
        atendimento: ViagemSuporteTecnico,
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
        ultimoKm: String,
        data: String,
        hora: String,
        route: String,
        navController: NavHostController
    ) {
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
            // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmSaida <= ultimoKm) {
            Toast.makeText(context, "O KM não pode ser inferior ao último informado", Toast.LENGTH_SHORT).show()
        } else {
            atendimento.dataSaida = data
            atendimento.horaSaida = hora
            atendimento.localSaida = localSaida
            atendimento.localAtendimento = localAtendimento
            atendimento.kmSaida = kmSaida
            atendimento.tecnicoId = userLoggedData?.id
            atendimento.tecnicoNome = "${userLoggedData?.nome} ${userLoggedData?.sobrenome}"
            atendimento.imgPerfil = userLoggedData?.imagem
            atendimento.statusService = "Em rota"
            viewModelScope.launch(Dispatchers.IO) {
                insert(atendimento)
                currentUserViewModel.update(
                    CurrentUser(
                        id = userLoggedData?.id.toString(),
                        ultimoKm = kmSaida,
                    )
                )
                currentUserServices.addUltimoKm(kmSaida)
            }
            reiniciarTela(route, navController)
        }

    }

    fun informarChegada(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser?,
        atendimento: ViagemSuporteTecnico,
        kmChegada: String,
        ultimoKm: String,
        data: String,
        hora: String,
        route: String,
        navController: NavHostController
    ) {
        // VERIFICA SE O CAMPO ESTA VAZIO
        if( kmChegada.isEmpty() ) {
            Toast.makeText(context, "Você precisa informar o KM ao chegar no local de atendimento para continuar", Toast.LENGTH_SHORT).show()
        // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if( kmChegada <= ultimoKm ) {
            Toast.makeText(context, "O KM não pode ser inferior ao último informado", Toast.LENGTH_SHORT).show()
        } else {
            if ( atendimento.statusService.equals("Em rota") ) {
                atendimento.dataChegada = data
                atendimento.horaChegada = hora
                atendimento.kmChegada = kmChegada
                atendimento.kmRodado = (kmChegada.toInt() - atendimento.kmSaida!!.toInt()).toString()
                atendimento.statusService = "Em andamento"
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimento)
                    currentUserViewModel.update(
                        CurrentUser(
                            id = userLoggedData?.id.toString(),
                            ultimoKm = kmChegada,
                        )
                    )
                    currentUserServices.addUltimoKm(kmChegada)
                }
            } else if( atendimento.statusService.equals("Em rota, retornando") ) {
                atendimento.dataChegadaRetorno = data
                atendimento.horaChegadaRetorno = hora
                atendimento.kmChegada = kmChegada
                atendimento.statusService = "Finalizado"
                currentUserServices.addUltimoKm(kmChegada)
                atendimento.kmRodado = (kmChegada.toInt() - atendimento.kmSaida!!.toInt()).toString()
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimento)
                    currentUserViewModel.update(
                        CurrentUser(
                            id = userLoggedData?.id.toString(),
                            ultimoKm = kmChegada,
                            kmBackup = kmChegada
                        )
                    )
                    currentUserServices.addUltimoKm(kmChegada)
                    currentUserServices.addKmBackup(kmChegada)
                }
            }
            reiniciarTela(route, navController)
        }
    }

    fun finalizarAtendimento() {

    }

}