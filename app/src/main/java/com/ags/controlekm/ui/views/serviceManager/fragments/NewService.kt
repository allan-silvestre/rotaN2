package com.ags.controlekm.ui.views.serviceManager.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ags.controlekm.R
import com.ags.controlekm.ui.components.dropDownMenu.SelectAddressDropDownMenu

@Composable
fun NewService(
    date: String,
    time: String,
    departureAddress: (String) -> Unit,
    serviceAddress: (String) -> Unit,
    departureKm: (String) -> Unit,
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        SelectAddressDropDownMenu(
            labelAddress = context.getString(R.string.departure_address_label),
            visibleAddress = true,
            SelectedAddres = {departureAddress(it)},
        )
        SelectAddressDropDownMenu(
            labelAddress = context.getString(R.string.arrival_address_label),
            labelKm = context.getString(R.string.departure_km_label),
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