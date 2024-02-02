package com.ags.controlekm.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.ags.controlekm.components.FormularioTextField
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.ViewModels.EnderecoAtendimentoViewModel
import com.ags.controlekm.functions.SaveLocalService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EnderecoAtendimentoEditView(
    item: EnderecoAtendimento,
    visible: Boolean,
    onSalvar: () -> Unit,
    onCancel: () -> Unit,
    enderecoAtendimentoViewModel: EnderecoAtendimentoViewModel = viewModel()
) {
    //VARIAVEL CONTROLADORA DE CONTEUDO
    var countContent by remember { mutableStateOf(0) }

    var id by remember { mutableStateOf(item.id) }

    var nome by remember { mutableStateOf(item.nome) }
    var nomeError by rememberSaveable { mutableStateOf(true) }

    var estado by remember { mutableStateOf(item.estado) }
    var estadoError by rememberSaveable { mutableStateOf(true) }

    var cidade by remember { mutableStateOf(item.cidade) }
    var cidadeError by rememberSaveable { mutableStateOf(true) }

    var bairro by remember { mutableStateOf(item.bairro) }
    var bairroError by rememberSaveable { mutableStateOf(true) }

    var logradouro by remember { mutableStateOf(item.logradouro) }
    var logradouroError by rememberSaveable { mutableStateOf(true) }

    var numero by remember { mutableStateOf(item.numero) }
    var numeroError by rememberSaveable { mutableStateOf(true) }

    //Visible Dialog
    val visibleDialogEdit = remember { mutableStateOf(visible) }

    val coroutineScope = rememberCoroutineScope()

    if (visibleDialogEdit.value) {
        Dialog(
            onDismissRequest = {  },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                                            .padding(32.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Editar local de atendimento",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        LazyColumn(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            item {
                                                FormularioTextField(
                                                    readOnly = false,
                                                    value = nome.toString(),
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
                                                FormularioTextField(
                                                    readOnly = false,
                                                    value = estado.toString(),
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
                                                FormularioTextField(
                                                    readOnly = false,
                                                    value = cidade.toString(),
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
                                                FormularioTextField(
                                                    readOnly = false,
                                                    value = bairro.toString(),
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
                                                    FormularioTextField(
                                                        modifier = Modifier.weight(0.7f),
                                                        readOnly = false,
                                                        value = logradouro.toString(),
                                                        onValueChange = {
                                                            logradouro = it.take(40)
                                                        },
                                                        label = "Logradouro",
                                                        visualTransformation = VisualTransformation.None,
                                                        keyboardType = KeyboardType.Text,
                                                        imeAction = ImeAction.Next,
                                                        capitalization = KeyboardCapitalization.None,
                                                        erro = logradouroError,
                                                        erroMensagem = ""
                                                    )
                                                    Spacer(modifier = Modifier.width(5.dp))
                                                    FormularioTextField(
                                                        modifier = Modifier.weight(0.3f),
                                                        readOnly = false,
                                                        value = numero.toString(),
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
                                                        if (
                                                            nome.toString().isEmpty() ||
                                                            estado.toString().isEmpty() ||
                                                            cidade.toString().isEmpty() ||
                                                            bairro.toString().isEmpty() ||
                                                            logradouro.toString().isEmpty() ||
                                                            !numeroError
                                                        ) {
                                                            // ERRO
                                                        } else {
                                                            val enderecoAtendimento: EnderecoAtendimento =
                                                                EnderecoAtendimento(
                                                                    id = id,
                                                                    nome = nome,
                                                                    estado = estado,
                                                                    cidade = cidade,
                                                                    bairro = bairro,
                                                                    logradouro = logradouro,
                                                                    numero = numero
                                                                )
                                                            coroutineScope.launch(Dispatchers.IO) {
                                                                enderecoAtendimentoViewModel.insert(enderecoAtendimento)
                                                            }
                                                            onSalvar()
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

            }// FIM CONTENT DIALOG
        )

    }
}