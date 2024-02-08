package com.ags.controlekm.components.DropDownMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.TimeToLeave
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
import com.ags.controlekm.components.TextField.FormularioTextField
import com.ags.controlekm.components.TextField.FormularioTextFieldMenu
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.ViewModels.AddressViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DropDownMenuAtendimento(
    addressViewModel: AddressViewModel = viewModel(),
    labelLocal: String? = null,
    labelKm: String? = null,
    hora: String? = null,
    data: String? = null,
    visibleLocal: Boolean = false,
    visibleKm: Boolean = false,
    localSelecionado: ((String) -> Unit)? = null,
    kmInformado: ((String) -> Unit)? = null,
) {
    val enderecosLocal: List<EnderecoAtendimento> by addressViewModel.allAddress.collectAsState(emptyList())
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
    var kmSaida by remember { mutableStateOf("") }
if(visibleLocal){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = Icons.Outlined.LocationOn,
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
            label = labelLocal!!,
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
                                localSelecionado!!(it)
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
                                localSelecionado!!(it)
                                expandedMenu = false
                            })
                    }
                }
            }
        }
    }
}
    if(visibleKm) {
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
                onValueChange = {
                    kmSaida = it.take(6)
                    kmInformado!!(it)
                },
                label = labelKm!!,
                visualTransformation = VisualTransformation.None,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None,
                erro = true,
                erroMensagem = ""
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.weight(0.4f),
                text = "Hora: ${hora}\n" + "Data: ${data}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}