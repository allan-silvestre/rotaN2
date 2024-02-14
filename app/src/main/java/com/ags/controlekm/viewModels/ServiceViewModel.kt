package com.ags.controlekm.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.firebaseRepositories.FirebaseServiceRepository
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.models.database.CurrentUser
import com.ags.controlekm.models.database.Service
import com.ags.controlekm.database.repositorys.ServiceRepository
import com.ags.controlekm.models.params.newServiceParams
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
    private val firebaseServiceRepository: FirebaseServiceRepository,
    private val validadeFields: ValidadeFields
) : ViewModel() {

    private var _visibleNewService = MutableStateFlow(false)
    val visibleNewService = _visibleNewService.asStateFlow()
    private var _visibleTraveling = MutableStateFlow(false)
    val visibleTraveling = _visibleTraveling.asStateFlow()
    private var _visibleInProgress = MutableStateFlow(false)
    val visibleInProgress = _visibleInProgress.asStateFlow()
    private var _visibleButtonDefault = MutableStateFlow(false)
    val visibleButtonDefault = _visibleButtonDefault.asStateFlow()
    private var _visibleButtonCancel = MutableStateFlow(false)
    val visibleButtonCancel = _visibleButtonCancel.asStateFlow()

    private var _contentText = MutableStateFlow("")
    val contentText = _contentText.asStateFlow()
    private var _buttonTitle = MutableStateFlow("")
    val buttonTitle = _buttonTitle.asStateFlow()

    private val _loading = mutableStateOf(false)
    val loading get() = _loading

    var servicesCurrentUser: Flow<List<Service>> = serviceRepository.getServicesCurrentUser()
    var currentUser = MutableStateFlow(CurrentUser())

    val currentService = flow<Service> {
        while (true) {
            val _currentService = MutableStateFlow(Service())
            serviceRepository.getcurrentService()!!.firstOrNull()?.let {
                _currentService.value = it
            }
            emit(_currentService.value)
            serviceManagerCardControlContent()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        Service()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                currentUserRepository.getCurrentUser().firstOrNull()?.let {
                    currentUser.value = it
                }
            }
            launch {
                firebaseServiceRepository.getAllServices().collect { serviceList ->
                    serviceList.forEach { service ->
                        serviceRepository.insert(service)
                    }
                }
            }
        }

    }

    fun newService(params: newServiceParams) {
        // CHECK IF ANY FIELD IS EMPTY
        if (validadeFields.validateFieldsNewService(
                params.departureAddress,
                params.serviceAddress,
                params.departureKm
            )
        ) {
            val newService = Service()

            newService.departureDate = params.date
            newService.departureTime = params.time
            newService.departureAddress = params.departureAddress
            newService.serviceAddress = params.serviceAddress
            newService.departureKm = params.departureKm
            newService.technicianId = FirebaseAuth.getInstance().currentUser!!.uid
            newService.technicianName = "${currentUser.value.name} ${currentUser.value.lastName}"
            newService.profileImgTechnician = currentUser.value.image.toString()
            newService.statusService = "Em rota"

            currentUser.value.lastKm = params.departureKm

            viewModelScope.launch(Dispatchers.IO) {
                    _loading.value = true
                    insert(newService)
                    currentUserRepository.update(currentUser.value)
                    firebaseCurrentUserRepository.updateLastKm(params.departureKm)
                    delay(1000L)
                    _loading.value = false
            }
        }
    }

    fun confirmarChegada(
        kmChegada: Int,
        data: String,
        hora: String,
    ) {
        // VERIFICA SE O CAMPO ESTA VAZIO
        if (kmChegada.toString().isEmpty()) {
            println("Você precisa informar o KM ao chegar no local de atendimento para continuar")
            // VERIFICA SE O KM INFORMADO É VALIDO DE ACORDO COM O ULTIMO KM INFORMADO
        } else if (kmChegada.toInt() < currentUser.value.lastKm!!.toInt()) {
            println("O KM não pode ser inferior ao último informado")
        } else {
            if (currentService.value.statusService.equals("Em rota")) {
                currentService.value.dateArrival = data
                currentService.value.timeArrival = hora
                currentService.value.arrivalKm = kmChegada
                currentService.value.kmDriven = (kmChegada - currentService.value.departureKm)
                currentService.value.statusService = "Em andamento"

                currentUser.value.lastKm = kmChegada

                executar(
                    function = {
                        viewModelScope.launch(Dispatchers.IO) {
                            update(currentService.value)
                            currentUserRepository.update(currentUser.value)
                            firebaseCurrentUserRepository.updateLastKm(kmChegada)
                        }
                    },
                    onError = {}
                )
            } else if (currentService.value.statusService.equals("Em rota, retornando")) {
                currentService.value.dateArrivalReturn = data
                currentService.value.timeCompletedReturn = hora
                currentService.value.arrivalKm = kmChegada
                currentService.value.statusService = "Finalizado"
                firebaseCurrentUserRepository.updateLastKm(kmChegada)
                currentService.value.kmDriven = (kmChegada - currentService.value.departureKm)

                currentUser.value.lastKm = kmChegada
                currentUser.value.kmBackup = kmChegada

                executar(
                    function = {
                        viewModelScope.launch(Dispatchers.IO) {
                            update(currentService.value)
                            currentUserRepository.update(currentUser.value)
                            firebaseCurrentUserRepository.updateLastKm(kmChegada)
                            firebaseCurrentUserRepository.updateKmBackup(kmChegada)
                        }
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
            },
            onError = {}
        )
    }


    fun cancelar(
        userLoggedData: CurrentUser,
        atendimentoAtual: Service,
    ) {
        if (atendimentoAtual.statusService.equals("Em rota, retornando")) {
            // CANCELA VIAGEM DE RETORNO
            atendimentoAtual.departureDateToReturn = ""
            atendimentoAtual.startTimeReturn = ""
            atendimentoAtual.addressReturn = ""
            atendimentoAtual.statusService = "Em andamento"

            userLoggedData.lastKm = userLoggedData.kmBackup

            viewModelScope.launch(Dispatchers.IO) {
                    _loading.value = true
                    update(atendimentoAtual)
                    delay(1000L)
                    _loading.value = false
            }
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
                    firebaseCurrentUserRepository.updateLastKm(userLoggedData.kmBackup)
                    delay(1000L)
                    _loading.value = false

            }
        }
    }

    fun novoAtendimento(
        userLoggedData: CurrentUser?,
        novoAtendimento: Service,
        localSaida: String,
        localAtendimento: String,
        kmSaida: Int,
        data: String,
        hora: String,
        atendimentoAtual: Service,
        resumoAtendimento: String,
    ) {
        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (localSaida.equals(null) || localAtendimento.equals(null) || kmSaida.toString()
                .equals(null)
        ) {
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
                    userLoggedData.kmBackup = userLoggedData.lastKm
                    // FINALIZA O ATENDIMENTO ATUAL
                    viewModelScope.launch(Dispatchers.IO) {
                        update(atendimentoAtual)
                        currentUserRepository.update(userLoggedData)
                        firebaseCurrentUserRepository.updateKmBackup(userLoggedData.lastKm)
                    }


                    userLoggedData.lastKm = kmSaida
                    //INICIA UM NOVO ATENDIMENTO
                    viewModelScope.launch(Dispatchers.IO) {
                        insert(novoAtendimento)
                        currentUserRepository.update(userLoggedData)
                        firebaseCurrentUserRepository.updateLastKm(kmSaida)
                    }
                },


                onError = {}
            )
        }
    }

    private fun serviceManagerCardControlContent() {
        when (currentService.value.statusService) {
            "" -> {
                _contentText.value = ""
                _buttonTitle.value = "Iniciar percurso"
                _visibleButtonDefault.value = true
                _visibleButtonCancel.value = false
                _visibleNewService.value = true
                _visibleTraveling.value = false
                _visibleInProgress.value = false
            }

            "Em rota" -> {
                _contentText.value =
                    "${currentService.value.statusService} entre ${currentService.value.departureAddress} \n e ${currentService.value.serviceAddress}"
                _buttonTitle.value = "Confirmar chegada"
                _visibleButtonDefault.value = true
                _visibleNewService.value = false
                _visibleTraveling.value = true
                _visibleInProgress.value = false
                _visibleButtonCancel.value = true
            }

            "Em rota, retornando" -> {
                _contentText.value =
                    "${currentService.value.statusService} de ${currentService.value.serviceAddress} \n para ${currentService.value.addressReturn}"
                _buttonTitle.value = "Confirmar chegada"
                _visibleButtonDefault.value = true
                _visibleButtonCancel.value = true
                _visibleNewService.value = false
                _visibleTraveling.value = true
                _visibleInProgress.value = false
            }

            "Em andamento" -> {
                _contentText.value = "Em andamento"
                _buttonTitle.value = "Finalizar Atendimento"
                _visibleButtonDefault.value = true
                _visibleNewService.value = false
                _visibleTraveling.value = false
                _visibleInProgress.value = true
                _visibleButtonCancel.value = true
            }
        }
    }

    private fun executar(function: () -> Unit, onError: () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            try {
                delay(2000)
                function()
            } catch (e: Exception) {
                onError()
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun insert(newService: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.insert(newService)
            firebaseServiceRepository.insert(newService)
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

