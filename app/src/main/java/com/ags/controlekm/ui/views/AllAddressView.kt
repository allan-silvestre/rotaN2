package com.ags.controlekm.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.components.Dialog.AddressAddDialog
import com.ags.controlekm.components.Dialog.EditAddressDialog
import com.ags.controlekm.functions.navigateSingleTopTo
import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.ViewModels.AddressViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AllAddressView(
    navController: NavHostController,
    addressViewModel: AddressViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val enderecosLocal: List<EnderecoAtendimento> by addressViewModel.allAddress.collectAsState(emptyList())

    var selectedItem by remember { mutableStateOf<EnderecoAtendimento?>(null) }

    val visibleShowDialogAdd = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(enderecosLocal.sortedBy { it.nome }) { item ->
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(horizontal = 16.dp)
                            .clickable {

                            },
                        text = item.nome.toString(),
                        fontSize = 12.sp
                    )
                    Icon(
                        modifier = Modifier
                            .weight(0.2f)
                            .size(15.dp)
                            .clickable { selectedItem = item },
                        imageVector = Icons.Filled.Edit,
                        contentDescription = ""
                    )
                    Icon(
                        modifier = Modifier
                            .weight(0.2f)
                            .size(15.dp)
                            .clickable {
                                coroutineScope.launch(Dispatchers.IO) {
                                    addressViewModel.delete(item)
                                }
                            },
                        imageVector = Icons.Filled.Delete,
                        contentDescription = ""
                    )
                }
                Divider(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp),
                )
            }
        }
        FloatingActionButton(
            onClick = {
                visibleShowDialogAdd.value = true
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }
    if(visibleShowDialogAdd.value){
        AddressAddDialog(
            visible = visibleShowDialogAdd.value,
            onSave = {
                navController.navigateSingleTopTo("enderecosAtendimento")
                visibleShowDialogAdd.value = false
            },
            onCancel = {
                navController.navigateSingleTopTo("enderecosAtendimento")
                visibleShowDialogAdd.value = false
            },
        )
    }

    // Se um item for selecionado, exibe a tela de edição
    selectedItem?.let { item ->
        EditAddressDialog(
            item = item,
            visible = true,
            onSalvar = {
                navController.navigateSingleTopTo("enderecosAtendimento")
                selectedItem = null
            },
            onCancel = {
                navController.navigateSingleTopTo("enderecosAtendimento")
                selectedItem = null
            },
        )
    }
}

