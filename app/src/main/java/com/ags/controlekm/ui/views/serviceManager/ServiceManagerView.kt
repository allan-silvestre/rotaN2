package com.ags.controlekm.ui.views.serviceManager

import android.annotation.SuppressLint
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ags.controlekm.ui.views.app.viewModel.AppViewModel
import com.ags.controlekm.ui.views.serviceManager.components.EmailVerifieldDialog
import com.ags.controlekm.ui.views.serviceManager.components.ServiceManagerCard
import com.ags.controlekm.ui.views.serviceManager.fragments.LastestServicesList
import com.ags.controlekm.ui.views.serviceManager.viewModel.ServiceViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ServiceManagerView(
    navController: NavHostController,
    appViewModel: AppViewModel = hiltViewModel<AppViewModel>(),
    serviceViewModel: ServiceViewModel = hiltViewModel<ServiceViewModel>()
) {
    val currentUser by appViewModel.currentUser.collectAsStateWithLifecycle(null)
    val services by serviceViewModel.servicesCurrentUser.collectAsStateWithLifecycle(emptyList())

    if(currentUser?.emailVerification == false) {
        EmailVerifieldDialog(onDismissRequest = {  })
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(serviceViewModel.currentUser.value.id)
            ServiceManagerCard()

            Spacer(modifier = Modifier.height(16.dp))

            LastestServicesList(services)
        }
    }
}





