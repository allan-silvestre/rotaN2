package com.ags.controlekm.components.Dialog

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.components.Buttons.ButtonDefault
import com.ags.controlekm.components.DropDownMenu.DropDownMenuAtendimento
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.ViewModels.CurrentUserViewModel
import com.ags.controlekm.database.ViewModels.ViagemSuporteTecnicoViewModel
import com.ags.controlekm.database.ViewModels.executarFuncaoViewModel
import com.ags.controlekm.functions.reiniciarTela
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FinalizarAtendimentoDialog(
    viagemSuporteTecnicoViewModel: ViagemSuporteTecnicoViewModel = viewModel(),
    currentUserViewModel: CurrentUserViewModel = viewModel(),
    executarFuncaoViewModel: executarFuncaoViewModel = viewModel(),
    navController: NavHostController,
    currentUserServices: CurrentUserServices,
    userLoggedData: CurrentUser?,
    atendimentoAtual: ViagemSuporteTecnico,
    novoAtendimento: ViagemSuporteTecnico,
    resumoAtendimento: String,
    data: String,
    hora: String,
    onDismissRequest: () -> Unit,
) {
    val route = stringResource(R.string.home)

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var visibleFinalizarOpcoes by remember { mutableStateOf(true) }
    var visibleRetornar by remember { mutableStateOf(false) }
    var visibleNovoAtendimento by remember { mutableStateOf(false) }

    var titleDialog by remember { mutableStateOf("Finalizar atendimento") }

    var local by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }

    var funcaoExecutada by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier.wrapContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Exibir carregamento
                if (executarFuncaoViewModel.carregando.value) {
                    visibleRetornar = false
                    visibleNovoAtendimento = false
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                    )
                }
                // OPÇÕES FINALIZAR ATENDIMENTO
                AnimatedVisibility(visibleFinalizarOpcoes) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = titleDialog,
                            fontWeight = FontWeight.SemiBold,
                        )
                        ButtonDefault("Retornar") {
                            titleDialog = "Para onde vai retornar?"
                            visibleRetornar = true
                            visibleNovoAtendimento = false
                            visibleFinalizarOpcoes = false
                        }
                        ButtonDefault("Novo atendimento") {
                            titleDialog = "Qual o local do novo atendimento?"
                            visibleNovoAtendimento = true
                            visibleRetornar = false
                            visibleFinalizarOpcoes = false
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
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = titleDialog,
                            fontWeight = FontWeight.SemiBold,
                        )
                        DropDownMenuAtendimento(
                            label = "Retornar para",
                            data = data,
                            hora = hora,
                            localSelecionado = { localSelecionado -> local = localSelecionado },
                            kmInformado = { kmInformado -> km = kmInformado }
                        )
                        ButtonDefault("Iniciar percurso") {
                            executarFuncaoViewModel.executarFuncao(
                                function = {
                                    viagemSuporteTecnicoViewModel.iniciarRetorno(
                                    atendimento = atendimentoAtual,
                                    localRetorno = local,
                                    resumoAtendimento = resumoAtendimento,
                                    data = data,
                                    hora = hora,
                                ) },
                                onExecuted = { result ->
                                    funcaoExecutada = true
                                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                                    reiniciarTela(route, navController)
                                },
                                onError = {
                                    Toast.makeText(context, "Erro durante a execução da função", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
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
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = titleDialog,
                        fontWeight = FontWeight.SemiBold,
                    )
                    DropDownMenuAtendimento(
                        label = "Local do atendimento",
                        data = data,
                        hora = hora,
                        localSelecionado = { localSelecionado -> local = localSelecionado },
                        kmInformado = { kmInformado -> km = kmInformado }
                    )
                    ButtonDefault("Iniciar percurso") {
                        executarFuncaoViewModel.executarFuncao(
                            function = {
                                // INICIA UM NOVO ATENDIMENTO
                                viagemSuporteTecnicoViewModel.iniciarViagem(
                                    currentUserViewModel = currentUserViewModel,
                                    currentUserServices = currentUserServices,
                                    userLoggedData = userLoggedData,
                                    novoAtendimento = novoAtendimento,
                                    localSaida = atendimentoAtual.localAtendimento.toString(),
                                    localAtendimento = local,
                                    kmSaida = km,
                                    data = data,
                                    hora = hora,
                                )

                                // FINALIZA ATENDIMENTO ATUAL
                                viagemSuporteTecnicoViewModel.finalizarAtendimento(
                                    currentUserViewModel = currentUserViewModel,
                                    currentUserServices = currentUserServices,
                                    userLoggedData = userLoggedData,
                                    atendimentoAtual = atendimentoAtual,
                                    resumoAtendimento = resumoAtendimento,
                                    data = data,
                                    hora = hora,
                                )
                                 },
                            onExecuted = { result ->
                                funcaoExecutada = true
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                                reiniciarTela(route, navController)
                            },
                            onError = {
                                Toast.makeText(context, "Erro durante a execução da função", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}