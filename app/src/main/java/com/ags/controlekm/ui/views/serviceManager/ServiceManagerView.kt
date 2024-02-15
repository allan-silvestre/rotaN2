package com.ags.controlekm.ui.views.serviceManager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.ui.views.serviceManager.components.ServiceManagerCard
import com.ags.controlekm.ui.views.serviceManager.fragments.LastestServicesList
import com.ags.controlekm.viewModels.service.ServiceViewModel

@Composable
fun ServiceManagerView(
    navController: NavHostController,
    serviceViewModel: ServiceViewModel = hiltViewModel()
) {
    val servicesCurrentUser by serviceViewModel.servicesCurrentUser.collectAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ServiceManagerCard()

        Spacer(modifier = Modifier.height(16.dp))

        LastestServicesList(servicesCurrentUser)
    }
}





