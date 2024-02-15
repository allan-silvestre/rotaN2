package com.ags.controlekm.ui.views.serviceManager.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ags.controlekm.ui.components.buttons.ButtonDefault
import com.ags.controlekm.ui.components.buttons.ButtonText
import com.ags.controlekm.ui.components.dropDownMenu.SelectAddressDropDownMenu
import com.ags.controlekm.ui.components.progress.LoadingCircular
import com.ags.controlekm.ui.components.text.TitleText
import com.ags.controlekm.viewModels.service.ServiceViewModel
import com.ags.controlekm.viewModels.service.modelsParams.FinishCurrentServiceAndGenerateNewServiceParams
import com.ags.controlekm.viewModels.service.modelsParams.StartReturnParams

@Composable
fun AfterServiceDialog(
    serviceSummary: String,
    date: String,
    time: String,
    onDismissRequest: () -> Unit,
    serviceViewModel: ServiceViewModel = hiltViewModel<ServiceViewModel>()
) {
    var visibleOptions by remember { mutableStateOf(true) }
    var visibleReturn by remember { mutableStateOf(false) }
    var visibleNewService by remember { mutableStateOf(false) }

    var titleDialog by remember { mutableStateOf("Finalizar atendimento") }

    var local by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }

    val paddingButton = 3.dp

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { TitleText(titleDialog) },
        text = {
            if (serviceViewModel.loading.value) {
                visibleReturn = false
                visibleNewService = false
                LoadingCircular()
            }
            // OPÇÕES FINALIZAR ATENDIMENTO
            AnimatedVisibility(visibleOptions) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ButtonText(modifier = Modifier.fillMaxWidth(0.5f), text = "Retornar") {
                        titleDialog = "Para onde vai retornar?"
                        visibleReturn = true
                        visibleNewService = false
                        visibleOptions = false
                    }
                    ButtonText(modifier = Modifier, text = "Novo atendimento") {
                        titleDialog = "Qual o local do novo atendimento?"
                        visibleNewService = true
                        visibleReturn = false
                        visibleOptions = false
                    }
                }
            }
            // INICIAR UM NOVO ATENDIMENTO
            AnimatedVisibility(visible = visibleNewService) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SelectAddressDropDownMenu(
                        labelAddress = "Local do atendimento",
                        labelKm = "KM de saída",
                        data = date,
                        time = time,
                        visibleAddress = true,
                        visibleKm = true,
                        SelectedAddres = { localSelecionado -> local = localSelecionado },
                        InformedKm = { kmInformado -> km = kmInformado }
                    )

                    ButtonDefault(
                        "Iniciar percurso",
                        padding = paddingButton
                    ) {
                        serviceViewModel.finishCurrentServiceAndGenerateNewService(
                           FinishCurrentServiceAndGenerateNewServiceParams(
                                departureAddress = serviceViewModel.currentService.value.departureAddress,
                                serviceAddress = local,
                                departureKm = km.toInt(),
                                date = date,
                                time = time,
                                serviceSummary = serviceSummary,
                            ),

                        )
                        onDismissRequest()
                    }
                }
            }
            // SELECIONAR CIDADE DE RETORNO
            AnimatedVisibility(visible = visibleReturn) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SelectAddressDropDownMenu(
                        labelAddress = "Retornar para",
                        data = date,
                        time = time,
                        visibleAddress = true,
                        SelectedAddres = { localSelecionado -> local = localSelecionado },
                    )
                    ButtonDefault(
                        "Iniciar percurso",
                        padding = paddingButton
                    ) {
                        serviceViewModel.startReturn(
                            StartReturnParams(
                                returnAddress = local,
                                serviceSummary = serviceSummary,
                                date = date,
                                time = time,
                            )

                        )
                        onDismissRequest()
                    }
                }
            }
        },
        confirmButton = {}
    )
}