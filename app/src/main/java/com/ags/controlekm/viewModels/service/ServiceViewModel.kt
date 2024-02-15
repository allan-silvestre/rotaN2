package com.ags.controlekm.viewModels.service

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.firebaseRepositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.firebaseRepositories.FirebaseServiceRepository
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.database.models.database.CurrentUser
import com.ags.controlekm.database.models.database.Service
import com.ags.controlekm.database.repositorys.ServiceRepository
import com.ags.controlekm.viewModels.service.modelsParams.ConfirmArrivalParams
import com.ags.controlekm.viewModels.service.modelsParams.FinishCurrentServiceAndGenerateNewServiceParams
import com.ags.controlekm.viewModels.service.modelsParams.NewServiceParams
import com.ags.controlekm.viewModels.service.modelsParams.StartReturnParams
import com.ags.controlekm.viewModels.service.validateFields.ValidadeFields
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

    fun newService(params: NewServiceParams) {
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

    fun confirmArrival(params: ConfirmArrivalParams) {
        _loading.value = true
        if (validadeFields.validateFieldsConfirmArrival(params.arrivalKm)) {
            if (currentService.value.statusService == "Em rota") {
                viewModelScope.launch {
                    try {
                        currentService.value.dateArrival = params.date
                        currentService.value.timeArrival = params.time
                        currentService.value.arrivalKm = params.arrivalKm
                        currentService.value.kmDriven =
                            (params.arrivalKm - currentService.value.departureKm)
                        currentService.value.statusService = "Em andamento"

                        currentUser.value.lastKm = params.arrivalKm

                        update(currentService.value)
                        currentUserRepository.update(currentUser.value)
                        firebaseCurrentUserRepository.updateLastKm(params.arrivalKm)
                        delay(1000L)
                        _loading.value = false
                    } finally {

                    }
                }
            } else if (currentService.value.statusService == "Em rota, retornando") {
                viewModelScope.launch {
                    try {
                        currentService.value.dateArrivalReturn = params.date
                        currentService.value.timeCompletedReturn = params.time
                        currentService.value.arrivalKm = params.arrivalKm
                        currentService.value.statusService = "Finalizado"
                        firebaseCurrentUserRepository.updateLastKm(params.arrivalKm)
                        currentService.value.kmDriven =
                            (params.arrivalKm - currentService.value.departureKm)

                        currentUser.value.lastKm = params.arrivalKm
                        currentUser.value.kmBackup = params.arrivalKm

                        update(currentService.value)
                        currentUserRepository.update(currentUser.value)
                        firebaseCurrentUserRepository.updateLastKm(params.arrivalKm)
                        firebaseCurrentUserRepository.updateKmBackup(params.arrivalKm)
                        delay(1000L)
                        _loading.value = false
                    } finally {

                    }
                }

            }
        }
    }

    fun startReturn(params: StartReturnParams) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val currentService = currentService.value

                currentService.dateCompletion = params.date
                currentService.CompletionTime = params.time

                currentService.description = params.serviceSummary
                currentService.departureDateToReturn = params.date
                currentService.startTimeReturn = params.time
                currentService.addressReturn = params.returnAddress
                currentService.statusService = "Em rota, retornando"

                update(currentService)

                delay(1000L)
                _loading.value = false
            }finally {

            }
        }
    }

    fun cancel() {
        _loading.value = true

        val currentUser = currentUser.value
        val currentService = currentService.value

        if (currentService.statusService == "Em rota, retornando") {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // CANCELA VIAGEM DE RETORNO
                    currentService.departureDateToReturn = ""
                    currentService.startTimeReturn = ""
                    currentService.addressReturn = ""
                    currentService.statusService = "Em andamento"

                    currentUser.lastKm = currentUser.kmBackup

                    update(currentService)
                    delay(1000L)
                    _loading.value = false
                }finally {}
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // NO ROOM
                    // DELETA O ATENDIMENTO ATUAL DA TABELA
                    delete(Service(currentService.id))
                    // NO ROOM
                    // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                    currentUserRepository.update(currentUser)
                    // NO FIREBASE
                    // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                    firebaseCurrentUserRepository.updateLastKm(currentUser.kmBackup)

                    delay(1000L)
                    _loading.value = false
                }finally {}
            }
        }
    }

    fun finishCurrentServiceAndGenerateNewService(params: FinishCurrentServiceAndGenerateNewServiceParams) {
        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
        if (validadeFields.validateFieldsFinishCurrentServiceAndGenerateNewService(
                params.departureAddress,
                params.serviceAddress,
                params.departureKm
            )
        ) {
            _loading.value = true

            val currentUser = currentUser.value
            val currentService = currentService.value
            val newService = Service()

            viewModelScope.launch {
                try {
                    // FINISH THE CURRENT SERVICE
                    delay(1000L)
                    launch {
                        currentService.statusService = "Finalizado"

                        currentUser.kmBackup = currentUser.lastKm

                        viewModelScope.launch(Dispatchers.IO) {
                            update(currentService)
                            currentUserRepository.update(currentUser)
                            firebaseCurrentUserRepository.updateKmBackup(currentUser.lastKm)
                        }
                    }

                    // START A NEW SERVICE
                    delay(2000L)
                    launch {
                        newService.departureDate = params.date
                        newService.departureTime = params.time
                        newService.departureAddress = params.departureAddress
                        newService.serviceAddress = params.serviceAddress
                        newService.departureKm = params.departureKm
                        newService.technicianId = currentUser.id
                        newService.technicianName = "${currentUser.name} ${currentUser.lastName}"
                        newService.profileImgTechnician = currentUser.image
                        newService.statusService = "Em rota"

                        currentUser.lastKm = params.departureKm

                        viewModelScope.launch(Dispatchers.IO) {
                            insert(newService)
                            currentUserRepository.update(currentUser)
                            firebaseCurrentUserRepository.updateLastKm(params.departureKm)
                        }
                    }
                    delay(3000L)
                    _loading.value = false
                } finally {
                }
            }
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

