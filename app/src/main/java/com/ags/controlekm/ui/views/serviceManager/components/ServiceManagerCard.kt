package com.ags.controlekm.ui.views.serviceManager.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ags.controlekm.database.models.database.Service
import com.ags.controlekm.viewModels.service.modelsParams.NewServiceParams
import com.ags.controlekm.ui.components.buttons.ButtonDefault
import com.ags.controlekm.ui.components.buttons.ButtonIcon
import com.ags.controlekm.ui.components.progress.LoadingCircular
import com.ags.controlekm.ui.components.text.TitleText
import com.ags.controlekm.ui.views.serviceManager.fragments.InProgress
import com.ags.controlekm.ui.views.serviceManager.fragments.NewService
import com.ags.controlekm.ui.views.serviceManager.fragments.Traveling
import com.ags.controlekm.viewModels.service.ServiceViewModel
import com.ags.controlekm.viewModels.service.modelsParams.ConfirmArrivalParams

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ServiceManagerCard(serviceViewModel: ServiceViewModel = hiltViewModel<ServiceViewModel>()) {

    var date by remember { mutableStateOf("00/00/0000") }
    var time by remember { mutableStateOf("00:00:00") }

    DateAndTime({ date = it }, { time = it })

    var departureKm by remember { mutableStateOf("0") }
    var arrivalKm by remember { mutableStateOf("0") }
    var serviceSummary by remember { mutableStateOf("") }
    var serviceSummaryError by remember { mutableStateOf(true) }
    var departureAddress by remember { mutableStateOf("") }
    var serviceAddress by remember { mutableStateOf("") }

    var contentText by remember { mutableStateOf(serviceViewModel.contentText) }
    var buttonTitle by remember { mutableStateOf(serviceViewModel.buttonTitle) }

    // VISIBLE DIALOGS
    var visibleAfterServiceDialog by remember { mutableStateOf(false) }
    var visibleCancelDialog by remember { mutableStateOf(false) }

    // VISIBLE BUTTONS
    val visibleButtonDefault by serviceViewModel.visibleButtonDefault.collectAsState(false)
    val visibleButtonCancel by serviceViewModel.visibleButtonCancel.collectAsState(false)

    val currentService by serviceViewModel.currentService.collectAsStateWithLifecycle(Service())

    val vNewService by serviceViewModel.visibleNewService.collectAsState(false)
    val vTraveling by serviceViewModel.visibleTraveling.collectAsState(false)
    val vInProgress by serviceViewModel.visibleInProgress.collectAsState(false)

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
                if(serviceViewModel.loading.value) {
                    LoadingCircular()
                } else {
                    AnimatedVisibility(vNewService) {
                        NewService(
                            date,
                            time,
                            { departureAddress = it },
                            { serviceAddress = it },
                            { departureKm = it })
                    }
                    AnimatedVisibility(vTraveling) {
                        Traveling(contentText.value, date, time) { arrivalKm = it }
                    }
                    AnimatedVisibility(vInProgress) {
                        InProgress(serviceSummaryError) { serviceSummary = it }
                    }
                }

            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(visible = visibleButtonDefault) {
                    ButtonDefault(
                        buttonTitle.value,
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        fraction = 1f,
                    ) {
                        if (currentService.statusService.isEmpty()) {
                            serviceViewModel.newService(
                                NewServiceParams(
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
                            serviceViewModel.confirmArrival(
                                ConfirmArrivalParams(
                                    arrivalKm = arrivalKm.toInt(),
                                    date = date,
                                    time = time,
                                )

                            )
                        } else if (currentService.statusService.equals("Em andamento")) {
                            if (serviceSummary.isNotEmpty() && serviceSummary.length > 5) {
                                serviceSummaryError = true
                                visibleAfterServiceDialog = true
                            } else {
                                serviceSummaryError = false
                                visibleAfterServiceDialog = false
                                println("Descrição em branco ou muito curta")
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
                    visibleCancelDialog = true
                }
            }
        }
    }
    // AlertDialog CancelDialog
    AnimatedVisibility(visibleCancelDialog) {
        CancelDialog(
            onDismissRequest = { visibleCancelDialog = false },
            title = {
                if (currentService.statusService.equals("Em rota, retornando")) {
                    TitleText("Cancelar o retorno para \n ${currentService.addressReturn}")
                } else {
                    TitleText("Cancelar o atendimento \n ${currentService.serviceAddress}")
                }
            },
            confirmButton = {
                serviceViewModel.cancel()
            }
        )
    }

    //AlertDialog AfterServiceDialog
    AnimatedVisibility(visibleAfterServiceDialog) {
        AfterServiceDialog(
            serviceSummary = serviceSummary,
            date = date,
            time = time,
            onDismissRequest = {
                visibleAfterServiceDialog = false
            }
        )
    }
}