package com.ags.controlekm.ui.views.serviceManager.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ags.controlekm.database.local.repositories.CurrentUserRepository
import com.ags.controlekm.database.local.repositories.ServiceRepository
import com.ags.controlekm.database.models.CurrentUser
import com.ags.controlekm.database.models.Service
import com.ags.controlekm.database.remote.repositories.FirebaseCurrentUserRepository
import com.ags.controlekm.database.remote.repositories.FirebaseServiceRepository
import com.ags.controlekm.ui.views.serviceManager.viewModel.modelsParams.ConfirmArrivalParams
import com.ags.controlekm.ui.views.serviceManager.viewModel.modelsParams.FinishCurrentServiceAndGenerateNewServiceParams
import com.ags.controlekm.ui.views.serviceManager.viewModel.modelsParams.NewServiceParams
import com.ags.controlekm.ui.views.serviceManager.viewModel.modelsParams.StartReturnParams
import com.ags.controlekm.ui.views.serviceManager.viewModel.validateFields.ValidateFields
import com.ags.controlekm.workers.serviceManager.CancelService
import com.ags.controlekm.workers.serviceManager.UpdateService
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
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
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val firebaseCurrentUserRepository: FirebaseCurrentUserRepository,
    private val firebaseServiceRepository: FirebaseServiceRepository,
    private val validateFields: ValidateFields,
    private val workManager: WorkManager,
    application: Application
) : AndroidViewModel(application) {

    val tagName = "serviceWork"

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
            serviceManagerCardContentControl()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        Service()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                currentUserRepository.getCurrentUser()!!.firstOrNull()?.let {
                    currentUser.value = it
                }
            }
            verifyPendentWork(tagName){
                if(it) {
                    launch {
                        firebaseServiceRepository.getCurrentUserServices().collect { servicesList ->
                            servicesList.forEach { services ->
                                serviceRepository.insert(services)
                            }
                        }
                    }
                }
            }
        }
    }

    fun newService(params: NewServiceParams) {
        if (validateFields.validateFieldsNewService(
                params.departureAddress,
                params.serviceAddress,
                params.departureKm
            )
        ) {

            val newService = Service()
            val currentUser = currentUser.value

            newService.departureDate = params.date
            newService.departureTime = params.time
            newService.departureAddress = params.departureAddress
            newService.serviceAddress = params.serviceAddress
            newService.departureKm = params.departureKm
            newService.technicianId = FirebaseAuth.getInstance().currentUser!!.uid
            newService.technicianName = "${currentUser.name} ${currentUser.lastName}"
            newService.profileImgTechnician = currentUser.image
            newService.statusService = "Em rota"

            currentUser.lastKm = params.departureKm

            viewModelScope.launch(Dispatchers.IO) {
                serviceRepository.update(newService)
                currentUserRepository.update(currentUser)

                registerServiceWorker<UpdateService>(newService, currentUser)
            }
        }
    }

    fun confirmArrival(params: ConfirmArrivalParams) {
        val currentService = currentService.value
        val currentUser = currentUser.value

        if (validateFields.validateFieldsConfirmArrival(params.arrivalKm)) {
            if (currentService.statusService == "Em rota") {
                currentService.dateArrival = params.date
                currentService.timeArrival = params.time
                currentService.arrivalKm = params.arrivalKm
                currentService.kmDriven = (params.arrivalKm - currentService.departureKm)
                currentService.statusService = "Em andamento"

                currentUser.lastKm = params.arrivalKm

                viewModelScope.launch(Dispatchers.IO) {
                    serviceRepository.update(currentService)
                    currentUserRepository.update(currentUser)

                    registerServiceWorker<UpdateService>(currentService, currentUser)
                }

            } else if (currentService.statusService == "Em rota, retornando") {
                currentService.dateArrivalReturn = params.date
                currentService.timeCompletedReturn = params.time
                currentService.arrivalKm = params.arrivalKm
                currentService.statusService = "Finalizado"
                currentService.kmDriven = (params.arrivalKm - currentService.departureKm)

                currentUser.lastKm = params.arrivalKm
                currentUser.kmBackup = params.arrivalKm

                viewModelScope.launch(Dispatchers.IO) {
                    serviceRepository.update(currentService)
                    currentUserRepository.update(currentUser)

                    registerServiceWorker<UpdateService>(currentService, currentUser)
                }
            }
        }
    }

    fun startReturn(params: StartReturnParams) {
        val currentService = currentService.value
        val currentUser = currentUser.value

        currentService.dateCompletion = params.date
        currentService.CompletionTime = params.time

        currentService.description = params.serviceSummary
        currentService.departureDateToReturn = params.date
        currentService.startTimeReturn = params.time
        currentService.addressReturn = params.returnAddress
        currentService.statusService = "Em rota, retornando"

        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.update(currentService)
            currentUserRepository.update(currentUser)

            registerServiceWorker<UpdateService>(currentService, currentUser)
        }
    }

    fun cancel() {
        if (currentService.value.statusService == "Em rota, retornando") {
            val currentService = currentService.value
            val currentUser = currentUser.value

            // CANCELA VIAGEM DE RETORNO
            currentService.departureDateToReturn = ""
            currentService.startTimeReturn = ""
            currentService.addressReturn = ""
            currentService.statusService = "Em andamento"

            currentUser.lastKm = currentUser.kmBackup

            viewModelScope.launch(Dispatchers.IO) {
                serviceRepository.update(currentService)
                currentUserRepository.update(currentUser)

                registerServiceWorker<UpdateService>(currentService, currentUser)
            }

        } else {
            val currentService = currentService.value
            val currentUser = currentUser.value

            currentUser.lastKm = currentUser.kmBackup

            viewModelScope.launch(Dispatchers.IO) {
                serviceRepository.delete(currentService)
                currentUserRepository.update(currentUser)

                registerServiceWorker<CancelService>(currentService, currentUser)
            }
        }
    }

    fun finishCurrentServiceAndGenerateNewService(params: FinishCurrentServiceAndGenerateNewServiceParams) {
        if (validateFields.validateFieldsFinishCurrentServiceAndGenerateNewService(
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
                    // FINISH THE CURRENT SERVICE
                    delay(1000L)
                    launch {
                        currentService.statusService = "Finalizado"

                        currentUser.kmBackup = currentUser.lastKm

                        viewModelScope.launch(Dispatchers.IO) {
                            serviceRepository.update(currentService)
                            currentUserRepository.update(currentUser)

                            registerServiceWorker<UpdateService>(currentService, currentUser)
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
                            serviceRepository.update(newService)
                            currentUserRepository.update(currentUser)

                            registerServiceWorker<UpdateService>(newService, currentUser)
                        }
                    }
                    delay(3000L)
                    _loading.value = false
            }
        }
    }

    private inline fun <reified T : ListenableWorker> registerServiceWorker(
        service: Service,
        currentUser: CurrentUser
    ) {
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val inputData = Data.Builder()
                .putString("service", Gson().toJson(service))
                .putString("currentUser", Gson().toJson(currentUser))
                .build()

            val workRequest = OneTimeWorkRequestBuilder<T>()
                .addTag(tagName)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
                .setInputData(inputData)
                .build()

            workManager.enqueueUniqueWork(
                service.id,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )

            delay(2000L)
            _loading.value = false
        }
    }

    private fun serviceManagerCardContentControl() {
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

    private fun verifyPendentWork(tag: String, callback: (Boolean) -> Unit) {

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                val workInfos = workManager.getWorkInfosByTag(tag).get()

                var pendentWork = false

                for (workInfo in workInfos) {
                    if (workInfo.state == WorkInfo.State.ENQUEUED ||
                        workInfo.state == WorkInfo.State.RUNNING) {
                        // Existem trabalhos pendentes com a tag específica
                        pendentWork = true
                        break
                    }
                }

                callback.invoke(!pendentWork)

            } catch (e: Exception) {
                println(e)
                callback.invoke(false)
            }
        }
    }

}

