package com.ags.controlekm.ui.views

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.ui.components.buttons.ButtonIcon
import com.ags.controlekm.ui.components.buttons.ButtonDefault
import com.ags.controlekm.ui.components.cards.LatestServicesCard
import com.ags.controlekm.ui.components.dialog.CancelAlertDialog
import com.ags.controlekm.ui.components.dialog.AfterServiceDialog
import com.ags.controlekm.ui.components.dropDownMenu.SelectAddressDropDownMenu
import com.ags.controlekm.ui.components.progress.LoadingCircular
import com.ags.controlekm.ui.components.text.ContentText
import com.ags.controlekm.ui.components.text.TitleText
import com.ags.controlekm.ui.components.textField.FormularioOutlinedTextField
import com.ags.controlekm.database.firebaseServices.CurrentUserServices
import com.ags.controlekm.models.Service
import com.ags.controlekm.viewModels.AddressViewModel
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.viewModels.ServiceViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeView(
    navController: NavHostController,
    currentUserViewModel: CurrentUserViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = hiltViewModel<ServiceViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()

    val handler = remember { Handler(Looper.getMainLooper()) }

    var hora by remember { mutableStateOf("00:00:00") }
    var data by remember { mutableStateOf("00/00/0000") }

    val userLoggedData by currentUserViewModel.currentUserData.collectAsState(null)
    val allTripsCurrentUser by serviceViewModel.allTripsCurrentUser.collectAsState(emptyList())
    val countContent by serviceViewModel.countContent.collectAsState(flowOf(0))
    val currentService by serviceViewModel.currentService.collectAsState()

    var currentUser by rememberSaveable { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val currentUserServices = CurrentUserServices(currentUser?.uid.toString())

    val context = LocalContext.current

    var kmSaida by remember { mutableStateOf("") }

    var kmChegada by remember { mutableStateOf("") }

    var resumoAtendimento by remember { mutableStateOf("") }
    var resumoAtendimentoError by remember { mutableStateOf(true) }

    var localSaida by remember { mutableStateOf("") }
    var localAtendimento by remember { mutableStateOf("") }

    var textStatus by remember { mutableStateOf("") }
    var textButton by remember { mutableStateOf("") }

    var visibleFinalizarDialog by remember { mutableStateOf(false) }

    var novoAtendimento by remember { mutableStateOf(Service()) }

    var visibleButtonDefault by remember { mutableStateOf(false) }
    var visibleAlertCancel by remember { mutableStateOf(false) }
    var visibleButtonCancel by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                handler.post {
                    val currentTime = System.currentTimeMillis()

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    data = dateFormat.format(currentTime)

                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    hora = timeFormat.format(currentTime)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    if (serviceViewModel.loading.value || countContent == 0) {
                        LoadingCircular()
                    } else if (countContent == 1) {
                        visibleButtonDefault = true
                        visibleButtonCancel = false
                        textStatus = ""
                        textButton = "Iniciar percurso"
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SelectAddressDropDownMenu(
                                labelAddress = "De (Local de saída)",
                                visibleAddress = true,
                                SelectedAddres = { localSelecionado ->
                                    localSaida = localSelecionado
                                },
                            )
                            SelectAddressDropDownMenu(
                                labelAddress = "Para (Local do atendimento)",
                                labelKm = "KM de saída",
                                data = data,
                                time = hora,
                                visibleAddress = true,
                                visibleKm = true,
                                SelectedAddres = { localSelecionado ->
                                    localAtendimento = localSelecionado
                                },
                                InformedKm = { kmInformado -> kmSaida = kmInformado }
                            )
                        }
                    } else if (countContent == 2) {
                        visibleButtonDefault = true
                        visibleButtonCancel = true
                        if (currentService.statusService.equals("Em rota")) {
                            textStatus =
                                "${currentService.statusService} entre ${currentService.departureAddress} \n e ${currentService.serviceAddress}"
                            textButton = "Confirmar chegada"
                        }
                        if (currentService.statusService.equals("Em rota, retornando")) {
                            textStatus =
                                "${currentService.statusService} de ${currentService.serviceAddress} \n para ${currentService.addressReturn}"
                            textButton = "Confirmar chegada"
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Atendimento",
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = textStatus,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            SelectAddressDropDownMenu(
                                labelKm = "KM da Chegada",
                                time = hora,
                                data = data,
                                visibleKm = true,
                                InformedKm = { kmInformado -> kmChegada = kmInformado }
                            )
                        }
                    } else if (countContent == 3) {
                        visibleButtonDefault = true
                        visibleButtonCancel = true
                        textStatus = "Em andamento..."
                        textButton = "Finalizar atendimento"
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TitleText("Atendimento")
                            Spacer(modifier = Modifier.height(24.dp))
                            ContentText(textStatus)
                            Spacer(modifier = Modifier.height(24.dp))
                            FormularioOutlinedTextField(
                                readOnly = false,
                                value = resumoAtendimento,
                                onValueChange = { resumoAtendimento = it.take(50) },
                                label = "Resumo do atendimento",
                                visualTransformation = VisualTransformation.None,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences,
                                erro = resumoAtendimentoError,
                                erroMensagem = ""
                            )
                        }
                        if (visibleFinalizarDialog) {
                            AfterServiceDialog(
                                currentUserServices = currentUserServices,
                                userLoggedData = userLoggedData,
                                atendimentoAtual = currentService,
                                novoAtendimento = novoAtendimento,
                                resumoAtendimento = resumoAtendimento,
                                data = data,
                                hora = hora,
                                onDismissRequest = {
                                    visibleFinalizarDialog = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    AnimatedVisibility(visible = visibleButtonDefault) {
                        ButtonDefault(
                            textButton,
                            enable = if (countContent == 0) false else true,
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            fraction = 1f,
                        ) {
                            when (countContent) {
                                1 -> {
                                    serviceViewModel.iniciarViagem(
                                        currentUserViewModel = currentUserViewModel,
                                        currentUserServices = currentUserServices,
                                        userLoggedData = userLoggedData!!,
                                        novoAtendimento = novoAtendimento,
                                        localSaida = localSaida,
                                        localAtendimento = localAtendimento,
                                        kmSaida = kmSaida,
                                        data = data,
                                        hora = hora,
                                    )
                                }

                                2 -> {
                                    serviceViewModel.confirmarChegada(
                                        currentUserViewModel = currentUserViewModel,
                                        currentUserServices = currentUserServices,
                                        userLoggedData = userLoggedData,
                                        atendimentoAtual = currentService,
                                        kmChegada = kmChegada,
                                        data = data,
                                        hora = hora,
                                    )
                                }

                                3 -> {
                                    if (resumoAtendimento.isNotEmpty() && resumoAtendimento.length > 5) {
                                        resumoAtendimentoError = true
                                        visibleFinalizarDialog = true
                                    } else {
                                        resumoAtendimentoError = false
                                        visibleFinalizarDialog = false
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
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(15.dp),
                text = "Últimos atendimentos",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            Icon(
                modifier = Modifier.padding(end = 15.dp),
                imageVector = Icons.Filled.ManageSearch,
                contentDescription = ""
            )
        }
        LazyRow (modifier = Modifier) {
            items(allTripsCurrentUser!!.sortedByDescending {
                SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault())
                    .parse(it.departureDate) }.take(5)) {
                LatestServicesCard(
                    data = it.dateCompletion.toString(),
                    address = it.serviceAddress.toString(),
                )
            }
        }
    }
    // AlertaDialog Cancelar
    if (visibleAlertCancel) {
        CancelAlertDialog(
            onDismissRequest = { visibleAlertCancel = false },
            title = {
                if (currentService.statusService.equals("Em rota, retornando")) {
                    TitleText("Cancelar o retorno para \n ${currentService.addressReturn}")
                } else {
                    TitleText("Cancelar o atendimento \n ${currentService.serviceAddress}")
                }
            },
            confirmButton = {
                serviceViewModel.cancelar(
                    currentUserViewModel = currentUserViewModel,
                    currentUserServices = currentUserServices,
                    userLoggedData = userLoggedData!!,
                    atendimentoAtual = currentService
                )
            }
        )
    }
}