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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.R
import com.ags.controlekm.components.Buttons.ButtonPadrao
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
import com.ags.controlekm.database.ViewModels.EnderecoAtendimentoViewModel
import com.ags.controlekm.database.ViewModels.ViagemSuporteTecnicoViewModel
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

    val handler = remember { Handler(Looper.getMainLooper()) }

    var hora by remember { mutableStateOf("00:00:00") }
    var data by remember { mutableStateOf("00/00/0000") }

    val allViagens: List<ViagemSuporteTecnico> by viagemSuporteTecnicoViewModel.allViagemSuporteTecnico.collectAsState(emptyList())

    val viagensCurrentUser: List<ViagemSuporteTecnico> by viagemSuporteTecnicoViewModel.allViagensCurrentUser.collectAsState(emptyList())

    val userLoggedData by currentUserViewModel.currentUserData.collectAsState(null)

    var currentUser by rememberSaveable { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val currentUserServices = CurrentUserServices(currentUser?.uid.toString())

    val enderecosLocal: List<EnderecoAtendimento> by enderecoAtendimentoViewModel.allEnderecoAtendimento.collectAsState(emptyList())

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
    var atendimentoAtual by remember { mutableStateOf(ViagemSuporteTecnico()) }

    var visibleButtonDefault by remember { mutableStateOf(false) }
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

    DisposableEffect(enderecosLocal) {
        val enderecos = coroutineScope.launch(Dispatchers.IO) {
            enderecosList = enderecosLocal.map { endereco ->
                endereco.toStringEnderecoAtendimento()
            }
        }

        coroutineScope.launch(Dispatchers.IO) {
            viagemSuporteTecnicoViewModel.getViagensCurrentUser(userLoggedData?.id.toString())
        }

        onDispose {
            enderecos.cancel()
        }
    }

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
                    if (viagemSuporteTecnicoViewModel.countContent.intValue == 1 || viagemSuporteTecnicoViewModel.loading.value) {
                        LoadingCircular()
                        viagemSuporteTecnicoViewModel.executar(
                            function = {
                                viagemSuporteTecnicoViewModel.countContent.intValue =
                                    viagemSuporteTecnicoViewModel.homeCountContent(
                                        viagemSuporte = viagensCurrentUser,
                                        currentUser = currentUser,
                                        atendimento = { atendimento -> atendimentoAtual = atendimento }
                                )

                            },
                            onExecuted = {},
                            onError = {}
                        )
                    } else if (viagemSuporteTecnicoViewModel.countContent.intValue == 2) {
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
                    } else if (viagemSuporteTecnicoViewModel.countContent.intValue == 3) {
                        visibleButtonDefault = true
                        visibleButtonCancel = true
                        if (atendimentoAtual.statusService.equals("Em rota")) {
                            textStatus =
                                "${atendimentoAtual.statusService} entre ${atendimentoAtual.localSaida} \n e ${atendimentoAtual.localAtendimento}"
                            textButton = "Confirmar chegada"
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
                            DropDownMenuAtendimento(
                                labelKm = "KM da Chegada",
                                hora = hora,
                                data = data,
                                visibleKm = true,
                                kmInformado = { kmInformado -> kmChegada = kmInformado }
                            )
                        }
                    } else if (viagemSuporteTecnicoViewModel.countContent.intValue == 4) {
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
                                atendimentoAtual = atendimentoAtual,
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
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            fraction = if (visibleButtonCancel) 0.5f else 1f
                        ) {
                            when (viagemSuporteTecnicoViewModel.countContent.intValue) {
                                2 -> {
                                    viagemSuporteTecnicoViewModel.iniciarViagem(
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

                                3 -> {
                                    viagemSuporteTecnicoViewModel.confirmarChegada(
                                        currentUserViewModel = currentUserViewModel,
                                        currentUserServices = currentUserServices,
                                        userLoggedData = userLoggedData,
                                        atendimentoAtual = atendimentoAtual,
                                        kmChegada = kmChegada,
                                        data = data,
                                        hora = hora,
                                    )
                                }

                                4 -> {
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
                    Spacer(modifier = Modifier.width(1.dp))
                    AnimatedVisibility(visible = visibleButtonCancel) {
                        ButtonPadrao(
                            "Cancelar",
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            color = MaterialTheme.colorScheme.error
                        ) {
                            viagemSuporteTecnicoViewModel.cancelar(
                                currentUserViewModel = currentUserViewModel,
                                currentUserServices = currentUserServices,
                                userLoggedData = userLoggedData!!,
                                atendimentoAtual = atendimentoAtual
                            )
                        }
                    }
                }
            }
        }

        LazyColumn( ) {
            items(viagensCurrentUser.sortedBy { it.dataChegada }) {
                Row {
                    Text(text = it.kmChegada.toString())
                }
            }
        }
        LazyColumn( ) {
            items(allViagens.sortedBy { it.kmChegada }) {
                Row {
                    //Text(text = it.kmChegada.toString())
                }
            }
        }
    }
}