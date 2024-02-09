package com.ags.controlekm.components.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ags.controlekm.components.textField.FormularioOutlinedTextField
import com.ags.controlekm.models.Address
import com.ags.controlekm.viewModels.AddressViewModel
import com.ags.controlekm.functions.checkOnlyNumbers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddressAddDialog(
    visible: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    addressViewModel: AddressViewModel = viewModel(),
) {
    //VARIAVEL CONTROLADORA DE CONTEUDO
    var countContent by remember { mutableStateOf(0) }

    var nome by remember { mutableStateOf("") }
    var nomeError by rememberSaveable { mutableStateOf(true) }

    var estado by remember { mutableStateOf("") }
    var estadoError by rememberSaveable { mutableStateOf(true) }

    var cidade by remember { mutableStateOf("") }
    var cidadeError by rememberSaveable { mutableStateOf(true) }

    var bairro by remember { mutableStateOf("") }
    var bairroError by rememberSaveable { mutableStateOf(true) }

    var logradouro by remember { mutableStateOf("") }
    var logradouroError by rememberSaveable { mutableStateOf(true) }

    var numero by remember { mutableStateOf("") }
    var numeroError by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = { }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
            ) {
                AnimatedContent(targetState = countContent, label = "") { targetCount ->
                    when (targetCount) {
                        0 ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "Cadastrar endereço",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    item {
                                        FormularioOutlinedTextField(
                                            readOnly = false,
                                            value = nome,
                                            onValueChange = { nome = it.take(40) },
                                            label = "Descrição",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = nomeError,
                                            erroMensagem = ""
                                        )
                                    }
                                    item {
                                        FormularioOutlinedTextField(
                                            readOnly = false,
                                            value = estado,
                                            onValueChange = { estado = it.take(2) },
                                            label = "Estado",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = estadoError,
                                            erroMensagem = ""
                                        )
                                    }
                                    item {
                                        FormularioOutlinedTextField(
                                            readOnly = false,
                                            value = cidade,
                                            onValueChange = { cidade = it.take(40) },
                                            label = "Cidade",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = cidadeError,
                                            erroMensagem = ""
                                        )
                                    }
                                    item {
                                        FormularioOutlinedTextField(
                                            readOnly = false,
                                            value = bairro,
                                            onValueChange = { bairro = it.take(40) },
                                            label = "Bairro",
                                            visualTransformation = VisualTransformation.None,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.None,
                                            erro = bairroError,
                                            erroMensagem = ""
                                        )
                                    }
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            FormularioOutlinedTextField(
                                                modifier = Modifier.weight(0.7f),
                                                readOnly = false,
                                                value = logradouro,
                                                onValueChange = { logradouro = it.take(40) },
                                                label = "Logradouro",
                                                visualTransformation = VisualTransformation.None,
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next,
                                                capitalization = KeyboardCapitalization.None,
                                                erro = logradouroError,
                                                erroMensagem = ""
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            FormularioOutlinedTextField(
                                                modifier = Modifier.weight(0.3f),
                                                readOnly = false,
                                                value = numero,
                                                onValueChange = { numero = it.take(4) },
                                                label = "Nº",
                                                visualTransformation = VisualTransformation.None,
                                                keyboardType = KeyboardType.Number,
                                                imeAction = ImeAction.Next,
                                                capitalization = KeyboardCapitalization.None,
                                                erro = numeroError,
                                                erroMensagem = ""
                                            )

                                        }
                                    }
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = {
                                                numeroError = checkOnlyNumbers(numero)

                                                if (
                                                    nome.isEmpty() ||
                                                    estado.isEmpty() ||
                                                    cidade.isEmpty() ||
                                                    bairro.isEmpty() ||
                                                    logradouro.isEmpty() ||
                                                    !numeroError
                                                ) {

                                                } else {
                                                    val address: Address =
                                                        Address(
                                                        name = nome,
                                                        state = estado,
                                                        city = cidade,
                                                        district = bairro,
                                                        streetName = logradouro,
                                                        number = numero
                                                    )
                                                    coroutineScope.launch(Dispatchers.IO) {
                                                    addressViewModel.insert(address)
                                                    }
                                                    onSave()
                                                }
                                            }) {
                                                Text("Salvar")
                                            }
                                            TextButton(
                                                onClick = {
                                                    onCancel()
                                                }) {
                                                Text("Cancelar")
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
}