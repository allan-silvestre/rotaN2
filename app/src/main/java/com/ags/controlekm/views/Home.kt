package com.ags.controlekm.views

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.components.FormularioOutlinedTextField
import com.ags.controlekm.components.FormularioTextField
import com.ags.controlekm.components.FormularioTextFieldMenu
import com.ags.controlekm.database.FirebaseServices.CurrentUserServices
import com.ags.controlekm.database.Models.CurrentUser
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.ags.controlekm.database.ViewModels.EnderecoAtendimentoViewModel
import com.ags.controlekm.database.ViewModels.LoginViewModel
import com.ags.controlekm.database.ViewModels.ViagemSuporteTecnicoViewModel
import com.ags.controlekm.functions.navigateSingleTopTo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@Composable
fun Home(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel(),
    enderecoAtendimentoViewModel: EnderecoAtendimentoViewModel = viewModel(),
    viagemSuporteTecnicoViewModel: ViagemSuporteTecnicoViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    var countContent by remember { mutableStateOf(0) }

    var currentUser by rememberSaveable { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val userLoggedData by loginViewModel.currentUserData.collectAsState(null)

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

    var time by remember { mutableStateOf("00:00:00") }
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
                    time = timeFormat.format(currentTime)
                }
            }
        }
    }

    val context = LocalContext.current

    var visibleFinalizarDialog by remember { mutableStateOf(false) }
    var visibleFinalizarOpcoes by remember { mutableStateOf(true) }
    var visibleRetornar by remember { mutableStateOf(false) }
    var visibleNovoAtendimento by remember { mutableStateOf(false) }

    val progressIndicator = remember { mutableStateOf(false) }

    var kmSaida by remember { mutableStateOf("") }
    var kmSaidaError by remember { mutableStateOf(true) }

    var kmChegada by remember { mutableStateOf("") }
    var kmChegadaError by remember { mutableStateOf(true) }

    var descricao by remember { mutableStateOf("") }
    var descricaoError by remember { mutableStateOf(true) }

    var expandedSaida by remember { mutableStateOf(false) }
    var expandedAtendimento by remember { mutableStateOf(false) }
    var expandedlocalFinalizarAtendimento by remember { mutableStateOf(false) }
    var localSaida by remember { mutableStateOf("") }
    var localSaidaError by remember { mutableStateOf(true) }
    var localAtendimento by remember { mutableStateOf("") }
    var localAtendimentoError by remember { mutableStateOf(true) }
    var localFinalizarAtendimento by remember { mutableStateOf("") }
    var localFinalizarAtendimentoError by remember { mutableStateOf(true) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var textStatus by remember { mutableStateOf("") }
    var textButton by remember { mutableStateOf("") }

    var textTituloFinalizarAtendimento by remember { mutableStateOf("Finalizar atendimento") }

    var atendimento by remember { mutableStateOf(ViagemSuporteTecnico()) }

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
                                                    atendimento = it
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
                                            text = "Hora: ${time}\n" + "Data: ${data}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            //textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            2 -> {
                                if (atendimento.statusService.equals("Em rota")) {
                                    textStatus =
                                        "${atendimento.statusService} entre  ${atendimento.localSaida} \n e ${atendimento.localAtendimento}"
                                    textButton = "Confirmar chegada e iniciar o atendimento"
                                }

                                if (atendimento.statusService.equals("Em rota, retornando")) {
                                    textStatus =
                                        "${atendimento.statusService} de ${atendimento.localAtendimento} \n para ${atendimento.localRetorno}"
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
                                            text = "Hora: ${time}\n" + "Data: ${data}",
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
                                        value = descricao,
                                        onValueChange = { descricao = it.take(50) },
                                        label = "Resumo do atendimento",
                                        visualTransformation = VisualTransformation.None,
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next,
                                        capitalization = KeyboardCapitalization.Sentences,
                                        erro = descricaoError,
                                        erroMensagem = ""
                                    )
                                }

                                if (visibleFinalizarDialog) {
                                    FinalizarAtendimentoDialog(
                                        onDismissRequest = {
                                            visibleFinalizarDialog = false
                                            visibleFinalizarOpcoes = false
                                            visibleRetornar = false
                                            visibleNovoAtendimento = false
                                            textTituloFinalizarAtendimento = "Finalizar atendimento"
                                        }) {
                                        Column(
                                            modifier = Modifier,
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(20.dp),
                                                text = textTituloFinalizarAtendimento,
                                                fontWeight = FontWeight.SemiBold,
                                            )
                                            // OPÇÕES FINALIZAR ATENDIMENTO
                                            AnimatedVisibility(visibleFinalizarOpcoes) {
                                                Column(
                                                    modifier = Modifier,
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Button(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(start = 6.dp, end = 6.dp),
                                                        shape = RoundedCornerShape(
                                                            topStart = 5.dp,
                                                            topEnd = 5.dp,
                                                            bottomStart = 5.dp,
                                                            bottomEnd = 5.dp
                                                        ),
                                                        onClick = {
                                                            textTituloFinalizarAtendimento = "Para onde vai retornar?"
                                                            visibleRetornar = true
                                                            visibleNovoAtendimento = false
                                                            visibleFinalizarOpcoes = false
                                                        }) {
                                                        Text(
                                                            modifier = Modifier,
                                                            text = "Retornar",
                                                            fontWeight = FontWeight.SemiBold,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                    Button(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                bottom = 2.dp,
                                                                start = 6.dp,
                                                                end = 6.dp
                                                            ),
                                                        shape = RoundedCornerShape(
                                                            topStart = 5.dp,
                                                            topEnd = 5.dp,
                                                            bottomStart = 5.dp,
                                                            bottomEnd = 5.dp
                                                        ),
                                                        onClick = {
                                                            textTituloFinalizarAtendimento = "Qual o local do novo atendimento?"
                                                            visibleNovoAtendimento = true
                                                            visibleRetornar = false
                                                            visibleFinalizarOpcoes = false
                                                        }) {
                                                        Text(
                                                            modifier = Modifier,
                                                            text = "Novo atendimento",
                                                            fontWeight = FontWeight.SemiBold,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                            // SELECIONAR CIDADE DE RETORNO
                                            AnimatedVisibility(visible = visibleRetornar) {
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
                                                            value = localFinalizarAtendimento,
                                                            trailingIconVector = Icons.Filled.ArrowDropDown,
                                                            trailingOnClick = {
                                                                expandedlocalFinalizarAtendimento = !expandedlocalFinalizarAtendimento
                                                            },
                                                            onValueChange = {
                                                                expandedlocalFinalizarAtendimento = true
                                                                localFinalizarAtendimento = it
                                                            },
                                                            label = "Retornar para",
                                                            visualTransformation = VisualTransformation.None,
                                                            keyboardType = KeyboardType.Text,
                                                            imeAction = ImeAction.Next,
                                                            capitalization = KeyboardCapitalization.None,
                                                            erro = localFinalizarAtendimentoError,
                                                            erroMensagem = ""
                                                        )
                                                        DropdownMenu(
                                                            modifier = Modifier.wrapContentSize(),
                                                            expanded = expandedlocalFinalizarAtendimento,
                                                            properties = PopupProperties(focusable = false),
                                                            onDismissRequest = {
                                                                expandedlocalFinalizarAtendimento = false
                                                            }) {
                                                            LazyColumn(
                                                                modifier = Modifier
                                                                    .width(300.dp)
                                                                    .height(195.dp)
                                                            ) {
                                                                if (localFinalizarAtendimento.isNotEmpty()) {
                                                                    items(
                                                                        enderecosList.filter {
                                                                            it.lowercase()
                                                                                .contains(
                                                                                    localFinalizarAtendimento.lowercase()
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
                                                                                localFinalizarAtendimento = it
                                                                                expandedlocalFinalizarAtendimento =
                                                                                    false
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
                                                                                localFinalizarAtendimento = it
                                                                                expandedlocalFinalizarAtendimento =
                                                                                    false
                                                                            })
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    Button(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(start = 6.dp, end = 6.dp),
                                                        shape = RoundedCornerShape(
                                                            topStart = 5.dp,
                                                            topEnd = 5.dp,
                                                            bottomStart = 5.dp,
                                                            bottomEnd = 5.dp
                                                        ),
                                                        onClick = {
                                                            atendimento.dataConclusao = data
                                                            atendimento.horaConclusao = time

                                                            atendimento.descricao = descricao
                                                            atendimento.dataSaidaRetorno = data
                                                            atendimento.horaSaidaRetorno = time
                                                            atendimento.localRetorno = localFinalizarAtendimento

                                                            atendimento.statusService =
                                                                "Em rota, retornando"
                                                            textStatus = ""
                                                            coroutineScope.launch(Dispatchers.IO) {
                                                                viagemSuporteTecnicoViewModel.update(
                                                                    atendimento
                                                                )
                                                            }

                                                            visibleFinalizarDialog = false
                                                            visibleFinalizarOpcoes = false
                                                            visibleRetornar = false
                                                            visibleNovoAtendimento = false
                                                            textTituloFinalizarAtendimento =
                                                                "Qual seu proximo passo?"
                                                            reiniciarTela(navController)

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
                                                            value = localFinalizarAtendimento,
                                                            trailingIconVector = Icons.Filled.ArrowDropDown,
                                                            trailingOnClick = {
                                                                expandedlocalFinalizarAtendimento = !expandedlocalFinalizarAtendimento
                                                            },
                                                            onValueChange = {
                                                                expandedlocalFinalizarAtendimento = true
                                                                localFinalizarAtendimento = it
                                                            },
                                                            label = "Local do atendimento",
                                                            visualTransformation = VisualTransformation.None,
                                                            keyboardType = KeyboardType.Text,
                                                            imeAction = ImeAction.Next,
                                                            capitalization = KeyboardCapitalization.None,
                                                            erro = localFinalizarAtendimentoError,
                                                            erroMensagem = ""
                                                        )
                                                        DropdownMenu(
                                                            modifier = Modifier.wrapContentSize(),
                                                            expanded = expandedlocalFinalizarAtendimento,
                                                            properties = PopupProperties(focusable = false),
                                                            onDismissRequest = { expandedlocalFinalizarAtendimento = false }) {
                                                            LazyColumn(
                                                                modifier = Modifier
                                                                    .width(300.dp)
                                                                    .height(195.dp)
                                                            ) {
                                                                if (localFinalizarAtendimento.isNotEmpty()) {
                                                                    items(
                                                                        enderecosList.filter {
                                                                            it.lowercase()
                                                                                .contains(
                                                                                    localFinalizarAtendimento.lowercase()
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
                                                                                localFinalizarAtendimento = it
                                                                                expandedlocalFinalizarAtendimento =
                                                                                    false
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
                                                                                localFinalizarAtendimento = it
                                                                                expandedlocalFinalizarAtendimento =
                                                                                    false
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
                                                            value = kmSaida,
                                                            onValueChange = { kmSaida = it.take(6) },
                                                            label = "KM da Saida",
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
                                                            text = "Hora: ${time}\n" + "Data: ${data}",
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
                                                            topStart = 5.dp,
                                                            topEnd = 5.dp,
                                                            bottomStart = 5.dp,
                                                            bottomEnd = 5.dp
                                                        ),
                                                        onClick = {
                                                            // FINALIZA O ATENDIMENTO ATUAL
                                                            atendimento.dataConclusao = data
                                                            atendimento.horaConclusao = time
                                                            atendimento.descricao = descricao
                                                            atendimento.statusService = "Finalizado"
                                                            textStatus = ""
                                                            coroutineScope.launch(Dispatchers.IO) {
                                                                viagemSuporteTecnicoViewModel.update(atendimento)
                                                            }
                                                            // INICIA O NOVO ATENDIMENTO AO FINALIZAR O ANTERIOR
                                                            // ( AINDA FALTA REAJUSTAR O CÓDIGO )
                                                            if (enderecosList.contains(localSaida) && kmSaida.isNotEmpty()) {
                                                                localAtendimento = atendimento.localAtendimento.toString()
                                                                if (localSaida.equals(localAtendimento)) {
                                                                    Toast.makeText(context, "O local de saída não pode ser igual ao local do atendimento", Toast.LENGTH_SHORT).show()
                                                                } else {
                                                                    if (kmSaida.toInt() > userLoggedData?.ultimoKm!!.toInt()) {
                                                                        kmChegadaError = true
                                                                        if (localSaidaError && localAtendimentoError && kmSaidaError) {
                                                                            atendimento.id = UUID.randomUUID().toString()
                                                                            atendimento.dataSaida = data
                                                                            atendimento.horaSaida = time
                                                                            atendimento.localSaida = localSaida
                                                                            atendimento.localAtendimento = localAtendimento
                                                                            atendimento.kmSaida = kmSaida
                                                                            atendimento.imgPerfil = userLoggedData?.imagem
                                                                            atendimento.tecnicoId = currentUser?.uid
                                                                            atendimento.tecnicoNome = "${userLoggedData?.nome} ${userLoggedData?.sobrenome}"
                                                                            atendimento.statusService = "Em rota"
                                                                            coroutineScope.launch(Dispatchers.IO) {
                                                                                viagemSuporteTecnicoViewModel.insert(atendimento)
                                                                                loginViewModel.insert(
                                                                                    CurrentUser(
                                                                                        id = userLoggedData?.id.toString(),
                                                                                        ultimoKm = kmSaida,
                                                                                    )
                                                                                )
                                                                                currentUserServices.addUltimoKm(kmSaida)
                                                                            }
                                                                            reiniciarTela(navController)
                                                                        } else {
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Não foi possivel iniciar sua viagem, verifique os campos e tente novamente",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                        }

                                                                    } else {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "KM inferior ao último informado.",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                        kmChegadaError = false
                                                                    }
                                                                }
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Não foi possivel iniciar sua viagem, verifique os campos e tente novamente",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }


                                                            // RESETE DE  VARIAVEIS
                                                            visibleFinalizarDialog = false
                                                            visibleFinalizarOpcoes = false
                                                            visibleRetornar = false
                                                            visibleNovoAtendimento = false
                                                            textTituloFinalizarAtendimento = "Finalizar atendimento"
                                                            reiniciarTela(navController)

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
                                        }
                                    }
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
                                if (enderecosList.contains(localSaida) && enderecosList.contains(
                                        localAtendimento
                                    ) && kmSaida.isNotEmpty()
                                ) {
                                    if (localSaida.equals(localAtendimento)) {
                                        Toast.makeText(
                                            context,
                                            "O local de saída não pode ser igual ao local do atendimento",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        if (kmSaida.toInt() > userLoggedData?.ultimoKm!!.toInt()) {
                                            kmChegadaError = true
                                            if (localSaidaError && localAtendimentoError && kmSaidaError) {
                                                atendimento.dataSaida = data
                                                atendimento.horaSaida = time
                                                atendimento.localSaida = localSaida
                                                atendimento.localAtendimento = localAtendimento
                                                atendimento.kmSaida = kmSaida
                                                atendimento.imgPerfil = userLoggedData?.imagem
                                                atendimento.tecnicoId = currentUser?.uid
                                                atendimento.tecnicoNome = "${userLoggedData?.nome} ${userLoggedData?.sobrenome}"
                                                atendimento.statusService = "Em rota"
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    viagemSuporteTecnicoViewModel.insert(atendimento)
                                                    loginViewModel.update(
                                                        CurrentUser(
                                                            id = userLoggedData?.id.toString(),
                                                            ultimoKm = kmSaida,
                                                        )
                                                    )
                                                    currentUserServices.addUltimoKm(kmSaida)
                                                }
                                                reiniciarTela(navController)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Não foi possivel iniciar sua viagem, verifique os campos e tente novamente",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        } else {
                                            Toast.makeText(
                                                context,
                                                "KM inferior ao último informado.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            kmChegadaError = false
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Não foi possivel iniciar sua viagem, verifique os campos e tente novamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            2 -> {
                                if (kmChegada.isEmpty()) {
                                    kmChegadaError = false
                                } else {
                                    kmChegadaError = true
                                    if (kmChegada.toInt() > userLoggedData?.ultimoKm!!.toInt()) {
                                        if (atendimento.statusService.equals("Em rota")) {
                                            atendimento.dataChegada = data
                                            atendimento.horaChegada = time
                                            atendimento.kmChegada = kmChegada
                                            atendimento.kmRodado = (kmChegada.toInt() - atendimento.kmSaida!!.toInt()).toString()
                                            atendimento.statusService = "Em andamento"
                                            coroutineScope.launch(Dispatchers.IO) {
                                                viagemSuporteTecnicoViewModel.update(atendimento)
                                                loginViewModel.update(
                                                    CurrentUser(
                                                        id = userLoggedData?.id.toString(),
                                                        ultimoKm = kmChegada,
                                                    )
                                                )
                                                currentUserServices.addUltimoKm(kmChegada)
                                            }
                                            reiniciarTela(navController)
                                        } else {
                                            atendimento.dataChegadaRetorno = data
                                            atendimento.horaChegadaRetorno = time
                                            atendimento.kmChegada = kmChegada
                                            atendimento.statusService = "Finalizado"
                                            currentUserServices.addUltimoKm(kmChegada)
                                            atendimento.kmRodado = (kmChegada.toInt() - atendimento.kmSaida!!.toInt()).toString()
                                            coroutineScope.launch(Dispatchers.IO) {
                                                viagemSuporteTecnicoViewModel.update(atendimento)
                                                loginViewModel.update(
                                                    CurrentUser(
                                                        id = userLoggedData?.id.toString(),
                                                        ultimoKm = kmChegada,
                                                        kmBackup = kmChegada
                                                    )
                                                )
                                                currentUserServices.addUltimoKm(kmChegada)
                                                currentUserServices.addKmBackup(kmChegada)
                                            }
                                            reiniciarTela(navController)
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "KM inferior ao último informado.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        kmChegadaError = false
                                    }
                                }
                            }

                            3 -> {
                                if (descricao.isNotEmpty() && descricao.length > 5) {
                                    descricaoError = true
                                    visibleFinalizarDialog = true
                                    visibleFinalizarOpcoes = true
                                } else {
                                    descricaoError = false
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
                viagemSuporteTecnicoViewModel.delete(ViagemSuporteTecnico(atendimento.id))

                // NO ROOM
                // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                loginViewModel.update(
                    CurrentUser(
                        id = userLoggedData?.id.toString(),
                        ultimoKm = userLoggedData?.kmBackup.toString(),
                    )
                )
                // NO FIREBASE
                // DEFINE O VALOR DO (ULTIMO KM) DO USUÁRIO PARA O ULTIMO INFORMADO AO CONCLUIR A ULTIMA VIAGEM
                currentUserServices.addUltimoKm(userLoggedData?.kmBackup.toString())
            }
            reiniciarTela(navController)
        }) {
            Text(text = "Cancelar atendimento")
        }

        TextButton(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                atendimento.dataSaidaRetorno = ""
                atendimento.horaSaidaRetorno = ""
                atendimento.localRetorno = ""

                atendimento.statusService = "Em andamento"
                textStatus = ""
                coroutineScope.launch(Dispatchers.IO) {
                    viagemSuporteTecnicoViewModel.update(
                        atendimento
                    )
                }
            }
            reiniciarTela(navController)
        }) {
            Text(text = "Cancelar retorno")
        }
    }
}


fun reiniciarTela(navController: NavHostController) {
    navController.popBackStack()
    navController.navigateSingleTopTo("home")
}

@Composable
fun FinalizarAtendimentoDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
        ) {
            content()
        }
    }

}