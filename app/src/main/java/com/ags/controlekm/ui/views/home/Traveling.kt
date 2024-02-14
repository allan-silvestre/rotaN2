package com.ags.controlekm.ui.views.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ags.controlekm.ui.components.dropDownMenu.SelectAddressDropDownMenu

@Composable
fun Traveling(
    title: String,
    date: String,
    time: String,
    arrivalKm: (String) -> Unit
) {
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
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        SelectAddressDropDownMenu(
            labelKm = "KM da Chegada",
            time = time,
            data = date,
            visibleKm = true,
            InformedKm = {arrivalKm(it)}
        )
    }
}