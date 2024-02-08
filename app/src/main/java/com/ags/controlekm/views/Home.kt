package com.ags.controlekm.views

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.components.Buttons.ButtonIcon
import com.ags.controlekm.components.Buttons.ButtonPadrao
import com.ags.controlekm.components.Cards.AtendimentoCard
import com.ags.controlekm.components.Dialog.AlertaDialogCancel
import com.ags.controlekm.components.Dialog.HomeAtendimentoDialog
import com.ags.controlekm.components.DropDownMenu.DropDownMenuAtendimento
import com.ags.controlekm.components.Progress.LoadingCircular
import com.ags.controlekm.components.Text.ContentText
import com.ags.controlekm.components.Text.TitleText
import com.ags.controlekm.components.TextField.FormularioOutlinedTextField
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.ViewModels.CurrentUserViewModel
import com.ags.controlekm.database.ViewModels.AddressViewModel
import com.ags.controlekm.database.ViewModels.ServiceViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Home(
    navController: NavHostController,
    currentUserViewModel: CurrentUserViewModel = viewModel(),
    addressViewModel: AddressViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = viewModel(ServiceViewModel::class.java),
) {
    val coroutineScope = rememberCoroutineScope()

    val handler = remember { Handler(Looper.getMainLooper()) }

    var hora by remember { mutableStateOf("00:00:00") }
    var data by remember { mutableStateOf("00/00/0000") }

    val userLoggedData by currentUserViewModel.currentUserData.collectAsState(null)
    val allTripsCurrentUser by serviceViewModel.allTripsCurrentUser.collectAsState(emptyList())
    val countContent by serviceViewModel.countContent.collectAsState(flowOf(0))
    val currentService by serviceViewModel.currentService.collectAsState()
    val enderecosLocal: List<EnderecoAtendimento> by addressViewModel.allAddress.collectAsState(emptyList())

    var currentUser by rememberSaveable { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val currentUserServices = CurrentUserServices(currentUser?.uid.toString())

    var enderecosList by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

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

    var novoAtendimento by remember { mutableStateOf(ViagemSuporteTecnico()) }

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
                            DropDownMenuAtendimento(
                                labelLocal = "De (Local de saída)",
                                visibleLocal = true,
                                localSelecionado = { localSelecionado ->
                                    localSaida = localSelecionado
                                },
                            )
                            DropDownMenuAtendimento(
                                labelLocal = "Para (Local do atendimento)",
                                labelKm = "KM de saída",
                                data = data,
                                hora = hora,
                                visibleLocal = true,
                                visibleKm = true,
                                localSelecionado = { localSelecionado ->
                                    localAtendimento = localSelecionado
                                },
                                kmInformado = { kmInformado -> kmSaida = kmInformado }
                            )
                        }
                    } else if (countContent == 2) {
                        visibleButtonDefault = true
                        visibleButtonCancel = true
                        if (currentService.statusService.equals("Em rota")) {
                            textStatus =
                                "${currentService.statusService} entre ${currentService.localSaida} \n e ${currentService.localAtendimento}"
                            textButton = "Confirmar chegada"
                        }
                        if (currentService.statusService.equals("Em rota, retornando")) {
                            textStatus =
                                "${currentService.statusService} de ${currentService.localAtendimento} \n para ${currentService.localRetorno}"
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
                            DropDownMenuAtendimento(
                                labelKm = "KM da Chegada",
                                hora = hora,
                                data = data,
                                visibleKm = true,
                                kmInformado = { kmInformado -> kmChegada = kmInformado }
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
                            HomeAtendimentoDialog(
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
                        ButtonPadrao(
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
                                    visibleButtonDefault = false
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
                    .parse(it.dataSaida) }.take(5)) {
                AtendimentoCard(
                    data = it.dataConclusao.toString(),
                    local = it.localAtendimento.toString(),
                    kmRodado = it.kmRodado.toString(),
                    status = it.statusService.toString()
                )
            }
        }
        Card (
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
            ),
            colors = CardDefaults.cardColors(
                //containerColor = Color(0xFF0B1F3C),
            )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                TitleText("Semana atual")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(195.dp)
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text("KM rodado:")
                    }
                    item {
                        Text("Valor a receber:")
                    }
                    item {
                        Text("KM rodado:")
                    }
                    item {
                        Text("")
                    }
                    item {
                        Text(countContent.toString())
                    }
                }
            }
        }
    }
    // AlertaDialog Cancelar
    if (visibleAlertCancel) {
        AlertaDialogCancel(
            onDismissRequest = { visibleAlertCancel = false },
            title = {
                if (currentService.statusService.equals("Em rota, retornando")) {
                    TitleText("Cancelar o retorno para \n ${currentService.localRetorno}")
                } else {
                    TitleText("Cancelar o atendimento \n ${currentService.localAtendimento}")
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