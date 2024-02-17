package com.ags.controlekm.ui.views.app

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.navigation.NavHostNavigation
import com.ags.controlekm.ui.views.app.fragments.BottomBar
import com.ags.controlekm.ui.views.app.fragments.BottomSheet
import com.ags.controlekm.ui.views.app.fragments.NavigationDrawer
import com.ags.controlekm.ui.views.app.fragments.TopBar
import com.ags.controlekm.viewModels.AppViewModel

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@Composable
fun App(
    appViewModel: AppViewModel = hiltViewModel<AppViewModel>()
) {
    val navController = rememberNavController()

    val itemsVisible by appViewModel.showAppbarAndBottomBar.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerContent = {
            AnimatedVisibility(visible = itemsVisible) {
                NavigationDrawer(navController, drawerState)
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                AnimatedVisibility(visible = itemsVisible) {
                    TopBar(drawerState, actionsOnClick = { showBottomSheet = true }) }
            },
            bottomBar = {
                AnimatedVisibility(visible = itemsVisible) {
                    BottomBar(navController)
                }
            }
        ) { innerPadding ->
             NavHostNavigation(innerPadding, navController)
        }
    }
    if (showBottomSheet) {
        BottomSheet(
            navController = navController,
            onDismissRequest = { showBottomSheet = false },
            onClick = { showBottomSheet = false }
        )
    }
}




