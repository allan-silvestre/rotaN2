package com.ags.controlekm.ui.views.serviceManager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.work.WorkManager
import com.ags.controlekm.ui.views.serviceManager.components.EmailVerifieldDialog
import com.ags.controlekm.ui.views.serviceManager.components.ServiceManagerCard
import com.ags.controlekm.ui.views.serviceManager.fragments.LastestServicesList
import com.ags.controlekm.ui.viewModels.CurrentUserViewModel
import com.ags.controlekm.ui.views.serviceManager.viewModel.ServiceViewModel
import java.util.UUID

@Composable
fun ServiceManagerView(
    navController: NavHostController,
    currentUserViewModel: CurrentUserViewModel = hiltViewModel<CurrentUserViewModel>(),
    serviceViewModel: ServiceViewModel = hiltViewModel<ServiceViewModel>()
) {
    val context = LocalContext.current

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





