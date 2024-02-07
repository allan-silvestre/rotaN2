package com.ags.controlekm.database.ViewModels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.AppDatabase
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.FirebaseServices.ViagemSuporteTecnicoServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.Repositorys.ViagemSuporteTecnicoRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViagemSuporteTecnicoViewModel(application: Application) : AndroidViewModel(application) {

    var context: Application = getApplication()

    private val _loading = mutableStateOf(false)
    val loading get() = _loading

    var START_CONTENT = 1

    private val _countContent = mutableIntStateOf(1)
    val countContent get() = _countContent

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("atendimentos")

    private val repository: ViagemSuporteTecnicoRepository
    val allViagemSuporteTecnico: Flow<List<ViagemSuporteTecnico>>
    private val viagemSuporteTecnicoServices: ViagemSuporteTecnicoServices

    lateinit var allViagensCurrentUser: Flow<List<ViagemSuporteTecnico>>

    init {
        val viagemSuporteTecnicoDao = AppDatabase.getDatabase(application).viagemSuporteTecnicoDao()
        this.repository = ViagemSuporteTecnicoRepository(viagemSuporteTecnicoDao)
        allViagemSuporteTecnico = repository.allViagemSuporteTecnico
        viewModelScope.launch{
            allViagensCurrentUser = repository.getViagensCurrentUser("")
        }
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

    // FUNÇÕES DE LÓGICA DE NEGOCIO
    fun iniciarViagem(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser,
        novoAtendimento: ViagemSuporteTecnico,
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
        data: String,
        hora: String,
    ) {
        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (localSaida.equals(null) || localAtendimento.equals(null) || kmSaida.equals(null)) {
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
        } else if (kmSaida.toInt() < userLoggedData.ultimoKm!!.toInt()) {
            Toast.makeText(
                context,
                "O KM não pode ser inferior ao último informado",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            novoAtendimento.dataSaida = data
            novoAtendimento.horaSaida = hora
            novoAtendimento.localSaida = localSaida
            novoAtendimento.localAtendimento = localAtendimento
            novoAtendimento.kmSaida = kmSaida
            novoAtendimento.tecnicoId = userLoggedData.id
            novoAtendimento.tecnicoNome = "${userLoggedData.nome} ${userLoggedData.sobrenome}"
            novoAtendimento.imgPerfil = userLoggedData.imagem.toString()
            novoAtendimento.statusService = "Em rota"

            userLoggedData.ultimoKm = kmSaida

            executar(
                function = {
                    viewModelScope.launch(Dispatchers.IO) {
                        insert(novoAtendimento)
                        currentUserViewModel.update(userLoggedData)
                        currentUserServices.addUltimoKm(kmSaida)
                    }
                },
                onExecuted = { _countContent.intValue = START_CONTENT },
                onError = {}
            )
        }
    }

    fun confirmarChegada(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser?,
        atendimentoAtual: ViagemSuporteTecnico,
        kmChegada: String,
        data: String,
        hora: String,
    ) {
        // VERIFICA SE O CAMPO ESTA VAZIO
        if (kmChegada.isEmpty()) {
            Toast.makeText(
                context,
                "Você precisa informar o KM ao chegar no local de atendimento para continuar",
                Toast.LENGTH_SHORT
            ).show()
            // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmChegada.toInt() < userLoggedData?.ultimoKm!!.toInt()) {
            Toast.makeText(
                context,
                "O KM não pode ser inferior ao último informado",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (atendimentoAtual.statusService.equals("Em rota")) {
                atendimentoAtual.dataChegada = data
                atendimentoAtual.horaChegada = hora
                atendimentoAtual.kmChegada = kmChegada
                atendimentoAtual.kmRodado =
                    (kmChegada.toInt() - atendimentoAtual.kmSaida!!.toInt()).toString()
                atendimentoAtual.statusService = "Em andamento"

                userLoggedData.ultimoKm = kmChegada

                executar(
                    function = {
                        viewModelScope.launch(Dispatchers.IO) {
                            update(atendimentoAtual)
                            currentUserViewModel.update(userLoggedData)
                            currentUserServices.addUltimoKm(kmChegada)
                        }

                    },
                    onExecuted = { countContent.intValue = START_CONTENT },
                    onError = {}
                )
            } else if (atendimentoAtual.statusService.equals("Em rota, retornando")) {
                atendimentoAtual.dataChegadaRetorno = data
                atendimentoAtual.horaChegadaRetorno = hora
                atendimentoAtual.kmChegada = kmChegada
                atendimentoAtual.statusService = "Finalizado"
                currentUserServices.addUltimoKm(kmChegada)
                atendimentoAtual.kmRodado =
                    (kmChegada.toInt() - atendimentoAtual.kmSaida!!.toInt()).toString()

                userLoggedData.ultimoKm = kmChegada
                userLoggedData.kmBackup = kmChegada

                executar(
                    function = {
                        viewModelScope.launch(Dispatchers.IO) {
                            update(atendimentoAtual)
                            currentUserViewModel.update(userLoggedData)
                            currentUserServices.addUltimoKm(kmChegada)
                            currentUserServices.addKmBackup(kmChegada)
                        }

                    },
                    onExecuted = { _countContent.intValue = START_CONTENT },
                    onError = {}
                )
            }
        }
    }

    fun iniciarRetorno(
        atendimento: ViagemSuporteTecnico,
        localRetorno: String,
        resumoAtendimento: String,
        data: String,
        hora: String,
    ) {
        atendimento.dataConclusao = data
        atendimento.horaConclusao = hora

        atendimento.descricao = resumoAtendimento
        atendimento.dataSaidaRetorno = data
        atendimento.horaSaidaRetorno = hora
        atendimento.localRetorno = localRetorno
        atendimento.statusService = "Em rota, retornando"

        executar(
            function = {
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimento)
                }

            },
            onExecuted = { _countContent.intValue = START_CONTENT },
            onError = {}
        )
    }

    fun finalizarAtendimento(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser,
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

        userLoggedData.kmBackup = userLoggedData?.ultimoKm.toString()

        executar(
            function = {
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimentoAtual)
                    currentUserViewModel.update(userLoggedData)
                    currentUserServices.addKmBackup(userLoggedData.ultimoKm.toString())
                }

            },
            onExecuted = { _countContent.intValue = START_CONTENT },
            onError = {}
        )
    }

    fun cancelar(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser,
        atendimentoAtual: ViagemSuporteTecnico,
    ) {
        executar(
            function = {
                if (atendimentoAtual.statusService.equals("Em rota, retornando")) {
                    // CANCELA VIAGEM DE RETORNO
                    atendimentoAtual.dataSaidaRetorno = ""
                    atendimentoAtual.horaSaidaRetorno = ""
                    atendimentoAtual.localRetorno = ""
                    atendimentoAtual.statusService = "Em andamento"

                    userLoggedData.ultimoKm = userLoggedData?.kmBackup.toString()

                    viewModelScope.launch(Dispatchers.IO) {
                        update(atendimentoAtual)
                    }
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        // NO ROOM
                        // DELETA O ATENDIMENTO ATUAL DA TABELA
                        delete(ViagemSuporteTecnico(atendimentoAtual.id))

                        // NO ROOM
                        // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                        currentUserViewModel.update(userLoggedData)
                        // NO FIREBASE
                        // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                        currentUserServices.addUltimoKm(userLoggedData.kmBackup.toString())
                    }
                }
            },
            onExecuted = { _countContent.intValue = START_CONTENT },
            onError = {}
        )
    }

    fun novoAtendimento(
        currentUserViewModel: CurrentUserViewModel,
        currentUserServices: CurrentUserServices,
        userLoggedData: CurrentUser?,
        novoAtendimento: ViagemSuporteTecnico,
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
        data: String,
        hora: String,
        atendimentoAtual: ViagemSuporteTecnico,
        resumoAtendimento: String,
    ) {
        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (localSaida.equals(null) || localAtendimento.equals(null) || kmSaida.equals(null)) {
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
            Toast.makeText(
                context,
                "O KM não pode ser inferior ao último informado",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            novoAtendimento.dataSaida = data
            novoAtendimento.horaSaida = hora
            novoAtendimento.localSaida = localSaida
            novoAtendimento.localAtendimento = localAtendimento
            novoAtendimento.kmSaida = kmSaida
            novoAtendimento.tecnicoId = userLoggedData.id
            novoAtendimento.tecnicoNome = "${userLoggedData.nome} ${userLoggedData.sobrenome}"
            novoAtendimento.imgPerfil = userLoggedData.imagem.toString()
            novoAtendimento.statusService = "Em rota"

            // FINALIZA O ATENDIMENTO ATUAL
            atendimentoAtual.dataConclusao = data
            atendimentoAtual.horaConclusao = hora
            atendimentoAtual.descricao = resumoAtendimento
            atendimentoAtual.statusService = "Finalizado"

            executar(
                function = {
                    userLoggedData.kmBackup = userLoggedData.ultimoKm.toString()
                    // FINALIZA O ATENDIMENTO ATUAL
                    viewModelScope.launch(Dispatchers.IO) {
                        update(atendimentoAtual)
                        currentUserViewModel.update(userLoggedData)
                        currentUserServices.addKmBackup(userLoggedData.ultimoKm.toString())
                    }

                },
                onExecuted = {
                    executar(
                        function = {
                            userLoggedData.ultimoKm = kmSaida
                            //INICIA UM NOVO ATENDIMENTO
                            viewModelScope.launch(Dispatchers.IO) {
                                insert(novoAtendimento)
                                currentUserViewModel.update(userLoggedData)
                                currentUserServices.addUltimoKm(kmSaida)
                            }
                        },
                        onExecuted = {},
                        onError = {}
                    )
                    _countContent.intValue = 0
                },
                onError = {}
            )
        }
    }

    fun executar(function: () -> Unit, onExecuted: (String) -> Unit, onError: () -> Unit) {
        // Verifique se já está carregando
        if (!loading.value) {
            // Iniciar o carregamento
            _loading.value = true

            viewModelScope.launch {
                try {
                    // Simular uma função assíncrona real
                    val resultado = withContext(Dispatchers.IO) {
                        delay(1000)
                        function()
                        _loading.value = false
                        "isCompleted"
                    }

                    // Chamar a função de retorno de sucesso
                    onExecuted(resultado)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Erro desconhecido, não foi possivél executar essa ação",
                        Toast.LENGTH_SHORT
                    ).show()
                    onError()
                } finally {
                    // Finalizar o carregamento, mesmo em caso de erro
                    _loading.value = false
                }
            }
        }
    }

    fun homeCountContent(
        viagemSuporte: List<ViagemSuporteTecnico>,
        currentUser: FirebaseUser?,
        atendimento: (ViagemSuporteTecnico) -> Unit
    ): Int {
        if (viagemSuporte.isNotEmpty()) {
            viagemSuporte.forEach {
                if (it.statusService!!.contains("Em rota") || it.statusService!!.contains("Em rota, retornando")) {
                    atendimento(it)
                    return 3
                } else if (it.statusService!!.contains("Em andamento")) {
                    atendimento(it)
                    return 4
                }
            }
        }
        return 2
    }

    fun getViagensCurrentUser(tecnicoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allViagensCurrentUser = repository.getViagensCurrentUser(tecnicoId)
        }
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
}

