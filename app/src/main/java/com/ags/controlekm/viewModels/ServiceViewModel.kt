package com.ags.controlekm.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.firebaseRepositories.FirebaseServiceRepository
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.models.CurrentUser
import com.ags.controlekm.models.Service
import com.ags.controlekm.database.repositorys.ServiceRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
    private val firebaseServiceRepository: FirebaseServiceRepository
): ViewModel() {

    private val _loading = mutableStateOf(false)
    val loading get() = _loading

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .reference
            .child("rotaN2")
            .child("services")

    var allServicesCurrentUser: Flow<List<Service>> =
        serviceRepository.getViagensCurrentUser(
            FirebaseAuth.getInstance().currentUser?.uid.toString()
        )

    var countContent: MutableStateFlow<Int> = MutableStateFlow(0)
    var currentService = MutableStateFlow(Service())

    var currentWeekData = mutableStateOf<List<Service>>(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseServiceRepository.getAllServices().collect{ serviceList ->
                serviceList.forEach { service ->
                    serviceRepository.insert(service)
                }
            }
        }

        homeCountContent()
    }

    // FUNÇÕES DE LÓGICA DE NEGOCIO
    fun iniciarViagem(
        userLoggedData: CurrentUser,
        novoAtendimento: Service,
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
        data: String,
        hora: String,
    ) {
        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (localSaida.equals(null) || localAtendimento.equals(null) || kmSaida.equals(null)) {
            println("Preencha todos os campos para continuar")
            // VERIFICA SE O LOCAL DE SAIDA É IGUAL AO LOCAL DE ATENDIMENTO
        } else if (localSaida.equals(localAtendimento)) {
            println("O local de saida não pode ser o mesmo local do atendimento")
            // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmSaida.toInt() < userLoggedData.lastKm!!.toInt()) {
            println("O KM não pode ser inferior ao último informado")
        } else {
            novoAtendimento.departureDate = data
            novoAtendimento.departureTime = hora
            novoAtendimento.departureAddress = localSaida
            novoAtendimento.serviceAddress = localAtendimento
            novoAtendimento.departureKm = kmSaida
            novoAtendimento.technicianId = userLoggedData.id
            novoAtendimento.technicianName = "${userLoggedData.name} ${userLoggedData.lastName}"
            novoAtendimento.profileImgTechnician = userLoggedData.image.toString()
            novoAtendimento.statusService = "Em rota"

            userLoggedData.lastKm = kmSaida

            executar(
                function = {
                    viewModelScope.launch(Dispatchers.IO) {
                        insert(novoAtendimento)
                        currentUserRepository.update(userLoggedData)
                        firebaseCurrentUserRepository.addUltimoKm(kmSaida)
                    }
                },
                onExecuted = {
                    if(it) {
                        countContent.value = 2
                        _loading.value = false
                    }
                },
                onError = {}
            )
        }
    }

    fun confirmarChegada(
        userLoggedData: CurrentUser?,
        atendimentoAtual: Service,
        kmChegada: String,
        data: String,
        hora: String,
    ) {
        // VERIFICA SE O CAMPO ESTA VAZIO
        if (kmChegada.isEmpty()) {
            println("Você precisa informar o KM ao chegar no local de atendimento para continuar")
            // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmChegada.toInt() < userLoggedData?.lastKm!!.toInt()) {
            println("O KM não pode ser inferior ao último informado")
        } else {
            if (atendimentoAtual.statusService.equals("Em rota")) {
                atendimentoAtual.dateArrival = data
                atendimentoAtual.timeArrival = hora
                atendimentoAtual.arrivalKm = kmChegada
                atendimentoAtual.kmDriven =
                    (kmChegada.toInt() - atendimentoAtual.departureKm!!.toInt()).toString()
                atendimentoAtual.statusService = "Em andamento"

                userLoggedData.lastKm = kmChegada

                executar(
                    function = {
                        viewModelScope.launch(Dispatchers.IO) {
                            update(atendimentoAtual)
                            currentUserRepository.update(userLoggedData)
                            firebaseCurrentUserRepository.addUltimoKm(kmChegada)
                        }
                    },
                    onExecuted = {
                        if(it) {
                            countContent.value = 3
                            _loading.value = false
                        }
                    },
                    onError = {}
                )
            } else if (atendimentoAtual.statusService.equals("Em rota, retornando")) {
                atendimentoAtual.dateArrivalReturn = data
                atendimentoAtual.timeCompletedReturn = hora
                atendimentoAtual.arrivalKm = kmChegada
                atendimentoAtual.statusService = "Finalizado"
                firebaseCurrentUserRepository.addUltimoKm(kmChegada)
                atendimentoAtual.kmDriven =
                    (kmChegada.toInt() - atendimentoAtual.departureKm!!.toInt()).toString()

                userLoggedData.lastKm = kmChegada
                userLoggedData.kmBackup = kmChegada

                executar(
                    function = {
                        viewModelScope.launch(Dispatchers.IO) {
                            update(atendimentoAtual)
                            currentUserRepository.update(userLoggedData)
                            firebaseCurrentUserRepository.addUltimoKm(kmChegada)
                            firebaseCurrentUserRepository.addKmBackup(kmChegada)
                            countContent.value = 1
                        }
                    },
                    onExecuted = {
                        if (it) { _loading.value = false}
                    },
                    onError = {}
                )
            }
        }
    }

    fun iniciarRetorno(
        atendimento: Service,
        localRetorno: String,
        resumoAtendimento: String,
        data: String,
        hora: String,
    ) {
        atendimento.dateCompletion = data
        atendimento.CompletionTime = hora

        atendimento.description = resumoAtendimento
        atendimento.departureDateToReturn = data
        atendimento.startTimeReturn = hora
        atendimento.addressReturn = localRetorno
        atendimento.statusService = "Em rota, retornando"

        executar(
            function = {
                viewModelScope.launch(Dispatchers.IO) {
                    update(atendimento)
                }
                homeCountContent()
            },
            onExecuted = {
                if(it) {
                    countContent.value = 2
                    _loading.value = false
                }
            },
            onError = {}
        )
    }

    fun cancelar(
        userLoggedData: CurrentUser,
        atendimentoAtual: Service,
    ) {
        var count: MutableStateFlow<Int> = MutableStateFlow(0)
        executar(
            function = {
                if (atendimentoAtual.statusService.equals("Em rota, retornando")) {
                    // CANCELA VIAGEM DE RETORNO
                    atendimentoAtual.departureDateToReturn = ""
                    atendimentoAtual.startTimeReturn = ""
                    atendimentoAtual.addressReturn = ""
                    atendimentoAtual.statusService = "Em andamento"

                    userLoggedData.lastKm = userLoggedData?.kmBackup.toString()

                    viewModelScope.launch(Dispatchers.IO) {
                        update(atendimentoAtual)
                    }
                    count.value = 3
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        // NO ROOM
                        // DELETA O ATENDIMENTO ATUAL DA TABELA
                        delete(Service(atendimentoAtual.id))

                        // NO ROOM
                        // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                        currentUserRepository.update(userLoggedData)
                        // NO FIREBASE
                        // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                        firebaseCurrentUserRepository.addUltimoKm(userLoggedData.kmBackup.toString())
                    }
                    count.value = 1
                }
            },
            onExecuted = {
                if(it) {
                    countContent.value = count.value
                    _loading.value = false
                }
            },
            onError = {}
        )
    }

    fun novoAtendimento(
        userLoggedData: CurrentUser?,
        novoAtendimento: Service,
        localSaida: String,
        localAtendimento: String,
        kmSaida: String,
        data: String,
        hora: String,
        atendimentoAtual: Service,
        resumoAtendimento: String,
    ) {
        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (localSaida.equals(null) || localAtendimento.equals(null) || kmSaida.equals(null)) {
            println("Preencha todos os campos para continuar")
            // VERIFICA SE O LOCAL DE SAIDA É IGUAL AO LOCAL DE ATENDIMENTO
        } else if (localSaida.equals(localAtendimento)) {
            println("O local de saida não pode ser o mesmo local do atendimento")
            // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmSaida.toInt() < userLoggedData?.lastKm!!.toInt()) {
            println("O KM não pode ser inferior ao último informado")
        } else {
            novoAtendimento.departureDate = data
            novoAtendimento.departureTime = hora
            novoAtendimento.departureAddress = localSaida
            novoAtendimento.serviceAddress = localAtendimento
            novoAtendimento.departureKm = kmSaida
            novoAtendimento.technicianId = userLoggedData.id
            novoAtendimento.technicianName = "${userLoggedData.name} ${userLoggedData.lastName}"
            novoAtendimento.profileImgTechnician = userLoggedData.image.toString()
            novoAtendimento.statusService = "Em rota"

            // FINALIZA O ATENDIMENTO ATUAL
            atendimentoAtual.dateCompletion = data
            atendimentoAtual.CompletionTime = hora
            atendimentoAtual.description = resumoAtendimento
            atendimentoAtual.statusService = "Finalizado"

            executar(
                function = {
                    userLoggedData.kmBackup = userLoggedData.lastKm.toString()
                    // FINALIZA O ATENDIMENTO ATUAL
                    viewModelScope.launch(Dispatchers.IO) {
                        update(atendimentoAtual)
                        currentUserRepository.update(userLoggedData)
                        firebaseCurrentUserRepository.addKmBackup(userLoggedData.lastKm.toString())
                    }
                },
                onExecuted = {
                    if(it) {
                        executar(
                            function = {
                                userLoggedData.lastKm = kmSaida
                                //INICIA UM NOVO ATENDIMENTO
                                viewModelScope.launch(Dispatchers.IO) {
                                    insert(novoAtendimento)
                                    currentUserRepository.update(userLoggedData)
                                    firebaseCurrentUserRepository.addUltimoKm(kmSaida)
                                }
                            },
                            onExecuted = {
                                if(it) {
                                    countContent.value = 2
                                    _loading.value = false
                                }
                            },
                            onError = {}
                        )
                    }
                },
                onError = {}
            )
        }
    }

    fun executar(function: () -> Unit, onExecuted: (Boolean) -> Unit, onError: () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Simular uma função assíncrona real
                val result = withContext(Dispatchers.IO) {
                    delay(1000)
                    function()
                    true
                }
                // Chamar a função de retorno de sucesso
                onExecuted(result)
            } catch (e: Exception) {
                println("Erro desconhecido, não foi possivél executar essa ação")
                onError()
            } finally {
                // Finalizar o carregamento, mesmo em caso de erro
                _loading.value = false
            }
        }
    }

    fun getViagensCurrentUser() {
        allServicesCurrentUser =
            serviceRepository.getViagensCurrentUser(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun homeCountContent() {
        var count: MutableStateFlow<Int> = MutableStateFlow(1)

        viewModelScope.launch {
            serviceRepository.getViagensCurrentUser(FirebaseAuth.getInstance().currentUser!!.uid).collect {
                it.forEach {
                    when {
                        it.statusService?.contains("Em rota") == true ||
                                it.statusService?.contains("Em rota, retornando") == true -> {
                            currentService.value = it
                            count = MutableStateFlow(2)
                        }

                        it.statusService?.contains("Em andamento") == true -> {
                            currentService.value = it
                            count = MutableStateFlow(3)
                        }
                    }
                }
                countContent = count
            }
        }
    }
    
    suspend fun insert(service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.insert(service)
            firebaseServiceRepository.insert(service)
        }
    }

    suspend fun update(service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.update(service)
            firebaseServiceRepository.update(service)
        }
    }

    suspend fun delete(service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.delete(service)
            firebaseServiceRepository.delete(service)
        }
    }
}

