package com.ags.controlekm.ui.views.serviceManager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ags.controlekm.database.models.database.CurrentUser
import com.ags.controlekm.ui.views.serviceManager.components.EmailVerifieldDialog
import com.ags.controlekm.ui.views.serviceManager.components.ServiceManagerCard
import com.ags.controlekm.ui.views.serviceManager.fragments.LastestServicesList
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.viewModels.service.ServiceViewModel

@Composable
fun ServiceManagerView(
    navController: NavHostController,
    currentUserViewModel: CurrentUserViewModel = hiltViewModel<CurrentUserViewModel>(),
    serviceViewModel: ServiceViewModel = hiltViewModel<ServiceViewModel>()
) {
    val currentUser by currentUserViewModel.currentUser.collectAsState(null)
    val services by serviceViewModel.servicesCurrentUser.collectAsState(emptyList())

    if(currentUser?.emailVerification == false) {
        EmailVerifieldDialog(onDismissRequest = {  })
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ServiceManagerCard()

            Spacer(modifier = Modifier.height(16.dp))

            LastestServicesList(services)
        }
    }
}





