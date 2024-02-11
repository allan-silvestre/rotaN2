package com.ags.controlekm.ui.components.dialog

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ags.controlekm.ui.components.buttons.ButtonDefault
import com.ags.controlekm.ui.components.buttons.ButtonText
import com.ags.controlekm.ui.components.dropDownMenu.SelectAddressDropDownMenu
import com.ags.controlekm.ui.components.progress.LoadingCircular
import com.ags.controlekm.ui.components.text.TitleText
import com.ags.controlekm.database.firebaseServices.CurrentUserServices
import com.ags.controlekm.models.CurrentUser
import com.ags.controlekm.models.Service
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.viewModels.ServiceViewModel
import com.ags.controlekm.viewModels.PerformFunctionViewModel
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun AfterServiceDialog(
    viagemSuporteTecnicoViewModel: ServiceViewModel = viewModel(),
    currentUserViewModel: CurrentUserViewModel = viewModel(),
    performFunctionViewModel: PerformFunctionViewModel = viewModel(),
    currentUserServices: CurrentUserServices,
    userLoggedData: CurrentUser?,
    atendimentoAtual: Service,
    novoAtendimento: Service,
    resumoAtendimento: String,
    data: String,
    hora: String,
    onDismissRequest: () -> Unit,
) {
    var visibleOpcoes by remember { mutableStateOf(true) }
    var visibleRetornar by remember { mutableStateOf(false) }
    var visibleNovoAtendimento by remember { mutableStateOf(false) }

    var titleDialog by remember { mutableStateOf("Finalizar atendimento") }

    var local by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }

    val paddingButton = 3.dp

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { TitleText(titleDialog) },
        text = {
            if (performFunctionViewModel.loading.value) {
                visibleRetornar = false
                visibleNovoAtendimento = false
                LoadingCircular()
            }
            // OPÇÕES FINALIZAR ATENDIMENTO
            AnimatedVisibility(visibleOpcoes) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ButtonText(modifier = Modifier.fillMaxWidth(0.5f), text = "Retornar") {
                        titleDialog = "Para onde vai retornar?"
                        visibleRetornar = true
                        visibleNovoAtendimento = false
                        visibleOpcoes = false
                    }
                    ButtonText(modifier = Modifier, text = "Novo atendimento") {
                        titleDialog = "Qual o local do novo atendimento?"
                        visibleNovoAtendimento = true
                        visibleRetornar = false
                        visibleOpcoes = false
                    }
                }
            }
            // INICIAR UM NOVO ATENDIMENTO
            AnimatedVisibility(visible = visibleNovoAtendimento) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SelectAddressDropDownMenu(
                        labelAddress = "Local do atendimento",
                        labelKm = "KM de saída",
                        data = data,
                        time = hora,
                        visibleAddress = true,
                        visibleKm = true,
                        SelectedAddres = { localSelecionado -> local = localSelecionado },
                        InformedKm = { kmInformado -> km = kmInformado }
                    )

                    ButtonDefault(
                        "Iniciar percurso",
                        padding = paddingButton
                    ) {
                        viagemSuporteTecnicoViewModel.novoAtendimento(
                            currentUserViewModel = currentUserViewModel,
                            currentUserServices = currentUserServices,
                            userLoggedData = userLoggedData,
                            novoAtendimento = novoAtendimento,
                            localSaida = atendimentoAtual.serviceAddress.toString(),
                            localAtendimento = local,
                            kmSaida = km,
                            data = data,
                            hora = hora,
                            atendimentoAtual = atendimentoAtual,
                            resumoAtendimento = resumoAtendimento,
                        )
                        onDismissRequest()
                    }
                }
            }
            // SELECIONAR CIDADE DE RETORNO
            AnimatedVisibility(visible = visibleRetornar) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SelectAddressDropDownMenu(
                        labelAddress = "Retornar para",
                        data = data,
                        time = hora,
                        visibleAddress = true,
                        SelectedAddres = { localSelecionado -> local = localSelecionado },
                    )
                    ButtonDefault(
                        "Iniciar percurso",
                        padding = paddingButton
                    ) {
                        viagemSuporteTecnicoViewModel.iniciarRetorno(
                            atendimento = atendimentoAtual,
                            localRetorno = local,
                            resumoAtendimento = resumoAtendimento,
                            data = data,
                            hora = hora,
                        )
                        onDismissRequest()
                    }
                }
            }
        },

        confirmButton = {}
    )

}