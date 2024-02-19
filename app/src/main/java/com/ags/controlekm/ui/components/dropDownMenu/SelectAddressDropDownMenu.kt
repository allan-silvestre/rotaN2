package com.ags.controlekm.ui.components.dropDownMenu

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ags.controlekm.ui.components.textField.FormularioTextField
import com.ags.controlekm.ui.components.textField.FormularioTextFieldMenu
import com.ags.controlekm.database.models.Address
import com.ags.controlekm.ui.views.addressManager.viewModel.AddressViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SelectAddressDropDownMenu(
    labelAddress: String? = null,
    labelKm: String? = null,
    time: String? = null,
    data: String? = null,
    visibleAddress: Boolean = false,
    visibleKm: Boolean = false,
    SelectedAddres: ((String) -> Unit)? = null,
    InformedKm: ((String) -> Unit)? = null,
    addressViewModel: AddressViewModel = hiltViewModel<AddressViewModel>(),
) {
    val allAddress: List<Address> by addressViewModel.allAddress.collectAsState(emptyList())
    var addressList by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    DisposableEffect(allAddress) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val enderecos = coroutineScope.launch {
            addressList = allAddress.map { endereco ->
                endereco.toStringEnderecoAtendimento()
            }
        }

        onDispose {
            enderecos.cancel()
        }
    }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expandedMenu by remember { mutableStateOf(false) }

    var address by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }

if(visibleAddress){
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
            value = address,
            trailingIconVector = Icons.Filled.ArrowDropDown,
            trailingOnClick = {
                expandedMenu = !expandedMenu
            },
            onValueChange = {
                expandedMenu = true
                address = it
            },
            label = labelAddress!!,
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
                if (address.isNotEmpty()) {
                    items(
                        addressList.filter {
                            it.lowercase().contains(address.lowercase()) || it.lowercase()
                                .contains("others")
                        }.sortedBy { it.lowercase() }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            onClick = {
                                address = it.toString()
                                SelectedAddres!!(it.toString())
                                expandedMenu = false
                            })
                    }
                } else {
                    items(
                        addressList.sortedBy { it.lowercase() }
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
                                address = it
                                SelectedAddres!!(it)
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
                value = departureTime,
                onValueChange = {
                    departureTime = it.take(6)
                    InformedKm!!(it)
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
                text = "Hora: ${time}\n" + "Data: ${data}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}