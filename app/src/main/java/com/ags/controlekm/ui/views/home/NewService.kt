package com.ags.controlekm.ui.views.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ags.controlekm.ui.components.dropDownMenu.SelectAddressDropDownMenu

@Composable
fun NewService(
    date: String,
    time: String,
    departureAddress: (String) -> Unit,
    serviceAddress: (String) -> Unit,
    departureKm: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SelectAddressDropDownMenu(
            labelAddress = "De (Local de saída)",
            visibleAddress = true,
            SelectedAddres = {departureAddress(it)},
        )
        SelectAddressDropDownMenu(
            labelAddress = "Para (Local do atendimento)",
            labelKm = "KM de saída",
            data = date,
            time = time,
            visibleAddress = true,
            visibleKm = true,
            SelectedAddres = { selectedAddress ->
                serviceAddress(selectedAddress)
            },
            InformedKm = {departureKm(it)}
        )
    }
}