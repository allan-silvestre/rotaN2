package com.ags.controlekm.database.ViewModels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.flowOf
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

    // FINALIZAR ATENDIMENTO
    var visibleFinalizarDialog: Flow<Boolean>
    var visibleFinalizarOpcoes = mutableStateOf(false)

    init {
        val viagemSuporteTecnicoDao = AppDatabase.getDatabase(application).viagemSuporteTecnicoDao()
        this.repository = ViagemSuporteTecnicoRepository(viagemSuporteTecnicoDao)
        allViagemSuporteTecnico = repository.allViagemSuporteTecnico
        viagemSuporteTecnicoServices = ViagemSuporteTecnicoServices()

        visibleFinalizarDialog = flowOf(false)

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
        novoAtendimento: ViagemSuporteTecnico,
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
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
        } else if (kmSaida.toInt() < userLoggedData?.ultimoKm!!.toInt()) {
            Toast.makeText(context, "O KM não pode ser inferior ao último informado", Toast.LENGTH_SHORT).show()
        } else {
            novoAtendimento.dataSaida = data
            novoAtendimento.horaSaida = hora
            novoAtendimento.localSaida = localSaida
            novoAtendimento.localAtendimento = localAtendimento
            novoAtendimento.kmSaida = kmSaida
            novoAtendimento.tecnicoId = userLoggedData?.id
            novoAtendimento.tecnicoNome = "${userLoggedData?.nome} ${userLoggedData?.sobrenome}"
            novoAtendimento.imgPerfil = userLoggedData?.imagem
            novoAtendimento.statusService = "Em rota"
            viewModelScope.launch(Dispatchers.IO) {
                insert(novoAtendimento)
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
        atendimentoAtual: ViagemSuporteTecnico,
        kmChegada: String,
        data: String,
        hora: String,
        route: String,
        navController: NavHostController
    ) {
        // VERIFICA SE O CAMPO ESTA VAZIO
        if( kmChegada.isEmpty() ) {
            Toast.makeText(context, "Você precisa informar o KM ao chegar no local de atendimento para continuar", Toast.LENGTH_SHORT).show()
        // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if( kmChegada.toInt() < userLoggedData?.ultimoKm!!.toInt() ) {
            Toast.makeText(context, "O KM não pode ser inferior ao último informado", Toast.LENGTH_SHORT).show()
        } else {
            if ( atendimentoAtual.statusService.equals("Em rota") ) {
                atendimentoAtual.dataChegada = data
                atendimentoAtual.horaChegada = hora
                atendimentoAtual.kmChegada = kmChegada
                atendimentoAtual.kmRodado = (kmChegada.toInt() - atendimentoAtual.kmSaida!!.toInt()).toString()
                atendimentoAtual.statusService = "Em andamento"
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimentoAtual)
                    currentUserViewModel.update(
                        CurrentUser(
                            id = userLoggedData?.id.toString(),
                            ultimoKm = kmChegada,
                        )
                    )
                    currentUserServices.addUltimoKm(kmChegada)
                }
            } else if( atendimentoAtual.statusService.equals("Em rota, retornando") ) {
                atendimentoAtual.dataChegadaRetorno = data
                atendimentoAtual.horaChegadaRetorno = hora
                atendimentoAtual.kmChegada = kmChegada
                atendimentoAtual.statusService = "Finalizado"
                currentUserServices.addUltimoKm(kmChegada)
                atendimentoAtual.kmRodado = (kmChegada.toInt() - atendimentoAtual.kmSaida!!.toInt()).toString()
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimentoAtual)
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

    fun iniciarRetorno(
        atendimento: ViagemSuporteTecnico,
        localRetorno: String,
        resumoAtendimento: String,
        data: String,
        hora: String,
        route: String,
        navController: NavHostController
    ){
        atendimento.dataConclusao = data
        atendimento.horaConclusao = hora

        atendimento.descricao = resumoAtendimento
        atendimento.dataSaidaRetorno = data
        atendimento.horaSaidaRetorno = hora
        atendimento.localRetorno = localRetorno
        atendimento.statusService = "Em rota, retornando"

        viewModelScope.launch(Dispatchers.IO) {
            update(atendimento)
        }

        reiniciarTela(route, navController)
    }

    fun finalizarAtendimento(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser?,
        atendimentoAtual: ViagemSuporteTecnico,
        resumoAtendimento: String,
        data: String,
        hora: String,
    ) {
        // FINALIZA O ATENDIMENTO ATUAL
        atendimentoAtual.dataConclusao = data
        atendimentoAtual.horaConclusao = hora
        atendimentoAtual.descricao = resumoAtendimento
        atendimentoAtual.statusService = "Finalizado"
        viewModelScope.launch(Dispatchers.IO) {
            update(atendimentoAtual)
            currentUserViewModel.update(
                CurrentUser(
                    id = userLoggedData?.id.toString(),
                    kmBackup = userLoggedData?.ultimoKm.toString()
                )
            )
            currentUserServices.addKmBackup(userLoggedData?.ultimoKm.toString())
        }
    }
}