package com.ags.controlekm.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ags.controlekm.components.TextField.FormularioTextFieldMenu
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.ViewModels.EnderecoAtendimentoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DropDownMenuAtendimento(
    enderecoAtendimentoViewModel: EnderecoAtendimentoViewModel = viewModel(),
    label: String? = "",
    onValueSelected: (String) -> Unit
) {
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
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expandedMenu by remember { mutableStateOf(false) }
    var local by remember { mutableStateOf("") }

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
                    textFieldSize = coordinates.size.toSize()
                },
            readOnly = false,
            value = local,
            trailingIconVector = Icons.Filled.ArrowDropDown,
            trailingOnClick = {
                expandedMenu = !expandedMenu
            },
            onValueChange = {
                expandedMenu = true
                local = it
            },
            label = label!!,
            visualTransformation = VisualTransformation.None,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.None,
            erro = true,
            erroMensagem = ""
        )

        DropdownMenu(
            modifier = Modifier.wrapContentSize(),
            expanded = expandedMenu,
            properties = PopupProperties(focusable = false),
            onDismissRequest = {
                expandedMenu = false
            }) {
            LazyColumn(
                modifier = Modifier
                    .width(300.dp)
                    .height(195.dp)
            ) {
                if (local.isNotEmpty()) {
                    items(
                        enderecosList.filter {
                            it.lowercase()
                                .contains(local.lowercase()) || it.lowercase()
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
                                local = it
                                onValueSelected(it)
                                expandedMenu = false
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
                                local = it
                                onValueSelected(it)
                                expandedMenu = false
                            })
                    }
                }
            }
        }
    }
}