package com.ags.controlekm.components.Dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ags.controlekm.R
import com.ags.controlekm.components.DropDownMenuAtendimento
import com.ags.controlekm.components.button.ButtonDefault
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.ViewModels.ViagemSuporteTecnicoViewModel

@Composable
fun FinalizarAtendimentoDialog(
    viagemSuporteTecnicoViewModel: ViagemSuporteTecnicoViewModel = viewModel(),
    atendimentoAtual: ViagemSuporteTecnico,
    resumoAtendimento: String,
    kmSaida: String,
    data: String,
    hora: String,
    onDismissRequest: () -> Unit,
    iniciarViagem: () -> Unit,
    finalizarAtendimento: () -> Unit,
) {
    var visibleFinalizarOpcoes by remember { mutableStateOf(true) }
    var visibleRetornar by remember { mutableStateOf(false) }
    var visibleNovoAtendimento by remember { mutableStateOf(false) }

    var titleDialog by remember { mutableStateOf("Finalizar atendimento") }

    var localRetorno by remember { mutableStateOf("") }

    val route = stringResource(R.string.home)
    var navController = rememberNavController()

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier.wrapContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = titleDialog + localRetorno,
                    fontWeight = FontWeight.SemiBold,
                )
                // OPÇÕES FINALIZAR ATENDIMENTO
                AnimatedVisibility(visibleFinalizarOpcoes) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                    Column {
                        DropDownMenuAtendimento(label = "Retornar para") {
                            localRetorno = it
                        }
                        ButtonDefault("Iniciar percurso") {
                            viagemSuporteTecnicoViewModel.iniciarRetorno(
                                atendimento = atendimentoAtual,
                                localRetorno = localRetorno,
                                resumoAtendimento = resumoAtendimento,
                                data = data,
                                hora = hora,
                                route = route,
                                navController = navController
                            )
                        }
                    }
                }
            }
            /***
            // INICIAR UM NOVO ATENDIMENTO
            AnimatedVisibility(visible = visibleNovoAtendimento) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Outlined.TravelExplore,
                            contentDescription = ""
                        )
                        FormularioTextFieldMenu(
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize =
                                        coordinates.size.toSize()
                                },
                            readOnly = false,
                            value = localAtendimento,
                            trailingIconVector = Icons.Filled.ArrowDropDown,
                            trailingOnClick = {
                                expanded = !expanded
                            },
                            onValueChange = {
                                expanded = true
                                localAtendimento = it
                            },
                            label = "Local do atendimento",
                            visualTransformation = VisualTransformation.None,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.None,
                            erro = localAtendimentoError,
                            erroMensagem = ""
                        )
                        DropdownMenu(
                            modifier = Modifier.wrapContentSize(),
                            expanded = expanded,
                            properties = PopupProperties(focusable = false),
                            onDismissRequest = { expanded = false }) {
                            LazyColumn(
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(195.dp)
                            ) {
                                if (localAtendimento.isNotEmpty()) {
                                    items(
                                        enderecos.filter {
                                            it.lowercase()
                                                .contains(
                                                    localAtendimento.lowercase()
                                                ) || it.lowercase()
                                                .contains("others")
                                        }.sorted()
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = it,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            },
                                            onClick = {
                                                localAtendimento = it
                                                expanded = false
                                            })
                                    }
                                } else {
                                    items(
                                        enderecos.sorted()
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = it,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            },
                                            onClick = {
                                                localAtendimento = it
                                                expanded = false
                                            })
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Outlined.TimeToLeave,
                            contentDescription = ""
                        )
                        FormularioTextField(
                            modifier = Modifier.weight(0.7f),
                            readOnly = false,
                            value = kmSaidaLocal,
                            onValueChange = { kmSaidaLocal = it.take(6) },
                            label = "KM da Saida",
                            visualTransformation = VisualTransformation.None,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.None,
                            erro = true,
                            erroMensagem = ""
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            modifier = Modifier.weight(0.4f),
                            text = "Hora: ${hora}\n" + "Data: ${data}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            //textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, end = 6.dp),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 5.dp,
                            bottomEnd = 5.dp
                        ),
                        onClick = {
                            iniciarViagem()
                            finalizarAtendimento()
                        }) {
                        Text(
                            modifier = Modifier,
                            text = "Iniciar percurso",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            ***/
        }
    }
}
