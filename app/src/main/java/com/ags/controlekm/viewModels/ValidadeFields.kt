package com.ags.controlekm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ags.controlekm.database.repositorys.CurrentUserRepository
import com.ags.controlekm.database.repositorys.ServiceRepository
import com.ags.controlekm.models.database.CurrentUser
import com.ags.controlekm.models.database.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ValidadeFields @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val currentUserRepository: CurrentUserRepository
): ViewModel() {
    var currentUser: CurrentUser = CurrentUser()
    var currentService: Service = Service()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                currentUserRepository.getCurrentUser().firstOrNull()?.let {
                    currentUser = it
                }
                serviceRepository.getcurrentService().firstOrNull()?.let {
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
}