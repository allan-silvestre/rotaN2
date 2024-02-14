package com.ags.controlekm.ui.views.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ags.controlekm.models.database.Service
import com.ags.controlekm.models.params.newServiceParams
import com.ags.controlekm.ui.components.buttons.ButtonDefault
import com.ags.controlekm.ui.components.buttons.ButtonIcon
import com.ags.controlekm.ui.components.progress.LoadingCircular
import com.ags.controlekm.ui.components.text.TitleText
import com.ags.controlekm.viewModels.ServiceViewModel

@Composable
fun ServiceManagerCard(serviceViewModel: ServiceViewModel = hiltViewModel()) {
    var time by remember { mutableStateOf("00:00:00") }
    var date by remember { mutableStateOf("00/00/0000") }

    val currentUser by serviceViewModel.currentU.collectAsStateWithLifecycle(null)
    val currentService by serviceViewModel.currentService.collectAsState(Service())

    val context = LocalContext.current

    var departureKm by remember { mutableStateOf("0") }

    var arrivalKm by remember { mutableStateOf("0") }

    var serviceSummary by remember { mutableStateOf("") }
    var serviceSummaryError by remember { mutableStateOf(true) }

    var departureAddress by remember { mutableStateOf("") }
    var serviceAddress by remember { mutableStateOf("") }

    var textStatus by remember { mutableStateOf("") }
    var textButton by remember { mutableStateOf("") }

    var visibleFinishDialog by remember { mutableStateOf(false) }

    //REMOVER ESSA VARIAVEL
    var novoAtendimento by remember { mutableStateOf(Service()) }

    var visibleButtonDefault by remember { mutableStateOf(false) }
    var visibleAlertCancel by remember { mutableStateOf(false) }
    var visibleButtonCancel by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .drawBehind {
                val cornerRadius = 40f

                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height / 1.5f)
                    arcTo(
                        rect = Rect(
                            left = size.width - 2 * cornerRadius,
                            top = size.height / 2 - cornerRadius,
                            right = size.width,
                            bottom = size.height / 2 + cornerRadius
                        ),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    lineTo(cornerRadius, size.height / 1.5f)
                    arcTo(
                        rect = Rect(
                            left = 0f,
                            top = size.height / 2 - cornerRadius,
                            right = 2 * cornerRadius,
                            bottom = size.height / 2 + cornerRadius
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    close()
                }

                drawPath(
                    path = path,
                    color = Color(0xFF0B1F3C)
                )

                /**
                drawRoundRect(
                color = Color(0xFF0B1F3C),
                size = Size(size.width, size.height / 1.7f),
                cornerRadius = CornerRadius(x = 32f, y = 32f)
                )
                 **/

                /**
                drawRoundRect(
                color = Color(0xFF0B1F3C),
                size = Size(size.width, size.height / 1.7f),
                cornerRadius = CornerRadius(x = 32f, y = 32f)
                )
                 **/
            },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(
                topStart = 30.dp,
                topEnd = 30.dp,
                bottomStart = 30.dp,
                bottomEnd = 30.dp
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (serviceViewModel.loading.value) {
                    LoadingCircular()
                } else if (currentService.statusService.isEmpty()) {
                    visibleButtonDefault = true
                    visibleButtonCancel = false
                    textStatus = ""
                    textButton = "Iniciar percurso"
                    NewService(date, time, {departureAddress = it}, {serviceAddress = it}, {departureKm = it})

                } else if (
                    currentService.statusService.equals("Em rota") ||
                    currentService.statusService.equals("Em rota, retornando")
                ) {
                    visibleButtonDefault = true
                    visibleButtonCancel = true
                    if (currentService?.statusService.equals("Em rota")) {
                        textStatus =
                            "${currentService?.statusService} entre ${currentService?.departureAddress} \n e ${currentService?.serviceAddress}"
                        textButton = "Confirmar chegada"
                    }
                    if (currentService?.statusService.equals("Em rota, retornando")) {
                        textStatus =
                            "${currentService?.statusService} de ${currentService?.serviceAddress} \n para ${currentService?.addressReturn}"
                        textButton = "Confirmar chegada"
                    }
                    Traveling(textStatus, date , time) { arrivalKm = it }

                } else if (currentService.statusService.equals("Em andamento")) {
                    visibleButtonDefault = true
                    visibleButtonCancel = true
                    textButton = "Finalizar atendimento"
                    InProgress(serviceSummaryError){serviceSummary = it}
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(visible = visibleButtonDefault) {
                    ButtonDefault(
                        textButton,
                        //enable = if (countContent == 0) false else true,
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        fraction = 1f,
                    ) {
                        if (currentService.statusService.isEmpty()) {
                            serviceViewModel.newService(
                                newServiceParams(
                                    departureAddress = departureAddress,
                                    serviceAddress = serviceAddress,
                                    departureKm = departureKm.toInt(),
                                    date = date,
                                    time = time,
                                ),
                            )
                        } else if (
                            currentService.statusService.equals("Em rota") ||
                            currentService.statusService.equals("Em rota, retornando")
                        ) {
                            serviceViewModel.confirmarChegada(
                                kmChegada = arrivalKm.toInt(),
                                data = date,
                                hora = time,
                            )
                        } else if (currentService.statusService.equals("Em andamento")) {
                            if (serviceSummary.isNotEmpty() && serviceSummary.length > 5) {
                                serviceSummaryError = true
                                visibleFinishDialog = true
                            } else {
                                serviceSummaryError = false
                                visibleFinishDialog = false
                                Toast.makeText(
                                    context,
                                    "Descrição em branco ou muito curta.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(visible = visibleButtonCancel) {
                ButtonIcon(
                    icon = Icons.Outlined.Cancel,
                    color = MaterialTheme.colorScheme.error
                ) {
                    visibleAlertCancel = true
                }
            }
        }
    }
    // AlertDialog Cancelar
    if (visibleAlertCancel) {
        CancelAlertDialog(
            onDismissRequest = { visibleAlertCancel = false },
            title = {
                if (currentService?.statusService.equals("Em rota, retornando")) {
                    TitleText("Cancelar o retorno para \n ${currentService?.addressReturn}")
                } else {
                    TitleText("Cancelar o atendimento \n ${currentService?.serviceAddress}")
                }
            },
            confirmButton = {
                serviceViewModel.cancelar(
                    userLoggedData = currentUser!!,
                    atendimentoAtual = currentService!!
                )
            }
        )
    }

    //AlertDialog Fininalizar
    if (visibleFinishDialog) {
        AfterServiceDialog(
            userLoggedData = currentUser,
            atendimentoAtual = currentService,
            novoAtendimento = novoAtendimento,
            resumoAtendimento = serviceSummary,
            data = date,
            hora = time,
            onDismissRequest = {
                visibleFinishDialog = false
            }
        )
    }
}