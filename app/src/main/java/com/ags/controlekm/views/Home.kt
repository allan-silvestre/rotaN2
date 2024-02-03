package com.ags.controlekm.views

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.TimeToLeave
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.components.Dialog.FinalizarAtendimentoDialog
import com.ags.controlekm.components.TextField.FormularioOutlinedTextField
import com.ags.controlekm.components.TextField.FormularioTextField
import com.ags.controlekm.components.TextField.FormularioTextFieldMenu
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.ViewModels.CurrentUserViewModel
import com.ags.controlekm.database.ViewModels.EnderecoAtendimentoViewModel
import com.ags.controlekm.database.ViewModels.ViagemSuporteTecnicoViewModel
import com.ags.controlekm.functions.reiniciarTela
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Home(
    navController: NavHostController,
    currentUserViewModel: CurrentUserViewModel = viewModel(),
    enderecoAtendimentoViewModel: EnderecoAtendimentoViewModel = viewModel(),
    viagemSuporteTecnicoViewModel: ViagemSuporteTecnicoViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    var countContent by rememberSaveable { mutableIntStateOf(0) }

    var currentUser by rememberSaveable { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val userLoggedData by currentUserViewModel.currentUserData.collectAsState(null)

    val currentUserServices = CurrentUserServices(currentUser?.uid.toString())

    val viagemSuporte: List<ViagemSuporteTecnico> by viagemSuporteTecnicoViewModel.allViagemSuporteTecnico.collectAsState(emptyList())

    val enderecosLocal: List<EnderecoAtendimento> by enderecoAtendimentoViewModel.allEnderecoAtendimento.collectAsState(emptyList())

    var enderecosList by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    DisposableEffect(enderecosLocal) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val enderecos = coroutineScope.launch {
            enderecosList = enderecosLocal.map { endereco ->
                endereco.toStringEnderecoAtendimento()
            }
        }

        onDispose {
            enderecos.cancel()
        }
    }

    var hora by remember { mutableStateOf("00:00:00") }
    var data by remember { mutableStateOf("00/00/0000") }

    val handler = remember { Handler(Looper.getMainLooper()) }

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

    val context = LocalContext.current
    val route = stringResource(R.string.home)

    var visibleFinalizarOpcoes by remember { mutableStateOf(true) }
    var visibleRetornar by remember { mutableStateOf(false) }
    var visibleNovoAtendimento by remember { mutableStateOf(false) }

    var textTituloFinalizarAtendimento by remember { mutableStateOf("Finalizar atendimento") }

    val progressIndicator = remember { mutableStateOf(false) }

    var kmSaida by remember { mutableStateOf("") }
    var kmSaidaError by remember { mutableStateOf(true) }

    var kmChegada by remember { mutableStateOf("") }
    var kmChegadaError by remember { mutableStateOf(true) }

    var resumoAtendimento by remember { mutableStateOf("") }
    var resumoAtendimentoError by remember { mutableStateOf(true) }

    var expandedSaida by remember { mutableStateOf(false) }
    var expandedAtendimento by remember { mutableStateOf(false) }

    var localSaida by remember { mutableStateOf("") }
    var localSaidaError by remember { mutableStateOf(true) }
    var localAtendimento by remember { mutableStateOf("") }
    var localAtendimentoError by remember { mutableStateOf(true) }
    var local by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf(true) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var textStatus by remember { mutableStateOf("") }
    var textButton by remember { mutableStateOf("") }

    var visibleFinalizarDialog by remember { mutableStateOf(false) }

    var novoAtendimento by remember { mutableStateOf(ViagemSuporteTecnico()) }
    var atendimentoAtual by remember { mutableStateOf(ViagemSuporteTecnico()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .drawBehind {
                    drawRect(
                        color = Color(0xFF005CBB),
                        size = Size(size.width, size.height / 1.7f)
                    )
                },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(start = 12.dp, end = 12.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(195.dp)
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedContent(
                        targetState = countContent,
                        label = "",
                    ) { targetCount ->
                        when (targetCount) {
                            0 -> {
                                progressIndicator.value = true
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    if (progressIndicator.value) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(50.dp),
                                            color = MaterialTheme.colorScheme.secondary,
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                        )
                                    }
                                    if (viagemSuporte.isNotEmpty()) {
                                        viagemSuporte.forEach {
                                            if (it.tecnicoId!!.contains(currentUser?.uid.toString())) {
                                                if (it.statusService != "Finalizado") {
                                                    atendimentoAtual = it
                                                    when (it.statusService) {
                                                        "Em rota" -> {
                                                            countContent = 2
                                                            progressIndicator.value = false
                                                        }

                                                        "Em rota, retornando" -> {
                                                            countContent = 2
                                                            progressIndicator.value = false
                                                        }

                                                        "Em andamento" -> {
                                                            countContent = 3
                                                            progressIndicator.value = false
                                                        }

                                                    }
                                                }

                                            }
                                        }
                                    } else {
                                        countContent = 1
                                        progressIndicator.value = false
                                    }
                                }
                            }

                            1 -> {// INICIAR ATENDIMENTO
                                textStatus = ""
                                textButton = "Iniciar percurso"
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
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
                                                    textFieldSize = coordinates.size.toSize()
                                                },
                                            readOnly = false,
                                            value = localSaida,
                                            trailingIconVector = Icons.Filled.ArrowDropDown,
                                            trailingOnClick = {
                                                expandedSaida = !expandedSaida
                                            },
                                            onValueChange = {
                                                expandedSaida = true
                                                localSaida = it
                                            },
                                            label = "De (Local de saída)",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = localSaidaError,
                                            erroMensagem = ""
                                        )
                                        DropdownMenu(
                                            modifier = Modifier.wrapContentSize(),
                                            expanded = expandedSaida,
                                            properties = PopupProperties(focusable = false),
                                            onDismissRequest = {
                                                expandedSaida = false
                                            }) {
                                            LazyColumn(
                                                modifier = Modifier
                                                    .width(300.dp)
                                                    .height(195.dp)
                                            ) {
                                                if (localSaida.isNotEmpty()) {
                                                    items(
                                                        enderecosList.filter {
                                                            it.lowercase()
                                                                .contains(localSaida.lowercase()) || it.lowercase()
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
                                                                localSaida = it
                                                                expandedSaida = false
                                                            })
                                                    }
                                                } else {
                                                    items(
                                                        enderecosList.sorted()
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
                                                                localSaida = it
                                                                expandedSaida = false
                                                            })
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
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
                                                    textFieldSize = coordinates.size.toSize()
                                                },
                                            readOnly = false,
                                            value = localAtendimento,
                                            trailingIconVector = Icons.Filled.ArrowDropDown,
                                            trailingOnClick = {
                                                expandedAtendimento = !expandedAtendimento
                                            },
                                            onValueChange = {
                                                expandedAtendimento = true
                                                localAtendimento = it
                                            },
                                            label = "Para (Local do atendimento)",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = localAtendimentoError,
                                            erroMensagem = ""
                                        )
                                        DropdownMenu(
                                            modifier = Modifier.wrapContentSize(),
                                            expanded = expandedAtendimento,
                                            properties = PopupProperties(focusable = false),
                                            onDismissRequest = {
                                                expandedAtendimento = false
                                            }) {
                                            LazyColumn(
                                                modifier = Modifier
                                                    .width(300.dp)
                                                    .height(195.dp)
                                            ) {
                                                if (localAtendimento.isNotEmpty()) {
                                                    items(
                                                        enderecosList.filter {
                                                            it.lowercase()
                                                                .contains(localAtendimento.lowercase()) || it.lowercase()
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
                                                                expandedAtendimento = false
                                                            })
                                                    }
                                                } else {
                                                    items(
                                                        enderecosList.sorted()
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
                                                                expandedAtendimento = false
                                                            })
                                                    }
                                                }
                                            }
                                        }

                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
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
                                            value = kmSaida,
                                            onValueChange = { kmSaida = it.take(6) },
                                            label = "KM da saída",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = kmSaidaError,
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
                                }
                            }

                            2 -> {
                                if (atendimentoAtual.statusService.equals("Em rota")) {
                                    textStatus =
                                        "${atendimentoAtual.statusService} entre  ${atendimentoAtual.localSaida} \n e ${atendimentoAtual.localAtendimento}"
                                    textButton = "Confirmar chegada e iniciar o atendimento"
                                }

                                if (atendimentoAtual.statusService.equals("Em rota, retornando")) {
                                    textStatus =
                                        "${atendimentoAtual.statusService} de ${atendimentoAtual.localAtendimento} \n para ${atendimentoAtual.localRetorno}"
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
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
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
                                            value = kmChegada,
                                            onValueChange = { kmChegada = it.take(6) },
                                            label = "KM da Chegada",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = kmChegadaError,
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
                                }
                            }

                            3 -> {
                                textStatus = "Em andamento..."
                                textButton = "Finalizar atendimento"
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
                                        modifier = Modifier.padding(30.dp),
                                        text = textStatus,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center
                                    )
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
                                    FinalizarAtendimentoDialog(
                                        atendimentoAtual = atendimentoAtual,
                                        resumoAtendimento = resumoAtendimento,
                                        kmSaida = kmSaida,
                                        data = data,
                                        hora = hora,
                                        onDismissRequest = {
                                            visibleFinalizarDialog = false
                                        },
                                        iniciarViagem = {
                                            viagemSuporteTecnicoViewModel.iniciarViagem(
                                                currentUserViewModel = currentUserViewModel,
                                                currentUserServices = currentUserServices,
                                                userLoggedData = userLoggedData,
                                                novoAtendimento = novoAtendimento,
                                                localSaida = atendimentoAtual.localAtendimento.toString(),
                                                localAtendimento = localAtendimento,
                                                kmSaida = kmSaida,
                                                data = data,
                                                hora = hora,
                                                route = route,
                                                navController = navController
                                            )
                                        },
                                        finalizarAtendimento = {
                                            viagemSuporteTecnicoViewModel.finalizarAtendimento(
                                                currentUserViewModel = currentUserViewModel,
                                                currentUserServices = currentUserServices,
                                                userLoggedData = userLoggedData,
                                                atendimentoAtual = atendimentoAtual,
                                                resumoAtendimento = resumoAtendimento,
                                                data = data,
                                                hora = hora,
                                            )

                                            visibleFinalizarDialog = false
                                            visibleFinalizarOpcoes = false
                                            visibleRetornar = false
                                            visibleNovoAtendimento = false
                                            textTituloFinalizarAtendimento = "Finalizar atendimento"
                                            reiniciarTela(route, navController)
                                        }

                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 5.dp,
                        bottomEnd = 5.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        when (countContent) {
                            1 -> {
                                viagemSuporteTecnicoViewModel.iniciarViagem(
                                    currentUserViewModel = currentUserViewModel,
                                    currentUserServices = currentUserServices,
                                    userLoggedData = userLoggedData,
                                    novoAtendimento = novoAtendimento,
                                    localSaida = localSaida,
                                    localAtendimento = localAtendimento,
                                    kmSaida = kmSaida,
                                    data = data,
                                    hora = hora,
                                    route = route,
                                    navController = navController
                                )
                            }
                            2 -> {
                                viagemSuporteTecnicoViewModel.informarChegada(
                                    currentUserViewModel = currentUserViewModel,
                                    currentUserServices = currentUserServices,
                                    userLoggedData = userLoggedData,
                                    atendimentoAtual = atendimentoAtual,
                                    kmChegada = kmChegada,
                                    data = data,
                                    hora = hora,
                                    route = route,
                                    navController = navController
                                )
                            }

                            3 -> {
                                if (resumoAtendimento.isNotEmpty() && resumoAtendimento.length > 5) {
                                    resumoAtendimentoError = true
                                    visibleFinalizarDialog = true
                                    visibleFinalizarOpcoes = true
                                } else {
                                    resumoAtendimentoError = false
                                    Toast.makeText(
                                        context,
                                        "Descrição em branco ou muito curta.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            4 -> {}
                        }
                    })
                {
                    Text(textButton)
                }
            }
        }
        TextButton(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                // DELETA O ATENDIMENTO ATUAL DA TABELA
                viagemSuporteTecnicoViewModel.delete(ViagemSuporteTecnico(atendimentoAtual.id))

                // NO ROOM
                // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                currentUserViewModel.update(
                    CurrentUser(
                        id = userLoggedData?.id.toString(),
                        ultimoKm = userLoggedData?.kmBackup.toString(),
                    )
                )
                // NO FIREBASE
                // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                currentUserServices.addUltimoKm(userLoggedData?.kmBackup.toString())
            }
            reiniciarTela(route, navController)
        }) {
            Text(text = "Cancelar atendimento")
        }

        TextButton(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                atendimentoAtual.dataSaidaRetorno = ""
                atendimentoAtual.horaSaidaRetorno = ""
                atendimentoAtual.localRetorno = ""

                atendimentoAtual.statusService = "Em andamento"
                textStatus = ""
                coroutineScope.launch(Dispatchers.IO) {
                    viagemSuporteTecnicoViewModel.update(
                        atendimentoAtual
                    )
                }
            }
            reiniciarTela(route, navController)
        }) {
            Text(text = "Cancelar retorno")
        }
    }
}