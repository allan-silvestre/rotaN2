package com.ags.controlekm.ui.views.serviceManager.viewModel.validateFields

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.local.repositories.CurrentUserRepository
import com.ags.controlekm.database.local.repositories.ServiceRepository
import com.ags.controlekm.database.models.CurrentUser
import com.ags.controlekm.database.models.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ValidateFields @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val currentUserRepository: CurrentUserRepository
): ViewModel() {
    var currentUser: CurrentUser = CurrentUser()
    var currentService: Service = Service()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                currentUserRepository.getCurrentUser()!!.firstOrNull()?.let {
                    currentUser = it
                }
                serviceRepository.getCurrentService(currentUser.id)!!.firstOrNull()?.let {
                    currentService = it
                }
            }
        }
    }

    fun validateFieldsNewService(
        departureAddress: String,
        serviceAddress: String,
        departureKm: Int
    ): Boolean {
        // CHECK IF ANY FIELD IS EMPTY
        if (departureAddress.isEmpty() || serviceAddress.isEmpty() || departureKm.toString().isEmpty()) {
            println("Preencha todos os campos para continuar")
            return false
            // CHECK IF THE DEPARTURE PLACE IS THE SAME AS THE SERVICE PLACE
        } else if (departureAddress.equals(serviceAddress)) {
            println("O local de saida não pode ser o mesmo do atendimento")
            return false
            // CHECK IF THE KM INFORMED IS VALID ACCORDING TO THE LAST KM INFORMED
        } else if (departureKm < currentUser.lastKm) {
            println("O KM não pode ser inferior ao último informado")
            return false
        } else if(currentService.statusService.isNotEmpty()) {
            println("Finalize o atendimento em aberto antes de iniciar um novo")
            return false
        }else { return true }
    }

    fun validateFieldsConfirmArrival(arrivalKM: Int): Boolean {
        if (arrivalKM.toString().isEmpty()) {
            println("Você precisa informar o KM ao chegar no local de atendimento para continuar")
            return false
        } else if (arrivalKM < currentUser.lastKm) {
            println("O KM não pode ser inferior ao último informado")
            return false
        } else {
            return true
        }
    }

    fun validateFieldsFinishCurrentServiceAndGenerateNewService(
        departureAddress: String,
        serviceAddress: String,
        departureKm: Int,
    ): Boolean {
        if (departureAddress.isEmpty() || serviceAddress.isEmpty() || departureKm.toString().isEmpty()) {
            println("Preencha todos os campos para continuar")
            return false
        } else if (departureAddress == serviceAddress) {
            println("O local de saida não pode ser o mesmo local do atendimento")
            return false
        } else if (departureKm < currentUser.lastKm) {
            println("O KM não pode ser inferior ao último informado")
            return false
        } else {
            return true
        }
    }
}