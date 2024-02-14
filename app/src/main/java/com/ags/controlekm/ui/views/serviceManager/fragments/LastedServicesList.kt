package com.ags.controlekm.ui.views.serviceManager.fragments

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ags.controlekm.models.database.Service
import com.ags.controlekm.ui.views.serviceManager.components.LatestServicesCard
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LastestServicesList(
    servicesCurrentUser: List<Service>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(15.dp),
            text = "Últimos atendimentos",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Icon(
            modifier = Modifier.padding(end = 15.dp),
            imageVector = Icons.Filled.ManageSearch,
            contentDescription = ""
        )
    }
    LazyRow(modifier = Modifier) {
        items(servicesCurrentUser.sortedByDescending {
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )
                .parse(it.departureDate)
        }.take(5)) {
            LatestServicesCard(
                data = it.dateCompletion.toString(),
                address = it.serviceAddress.toString(),
            )
        }
    }
}