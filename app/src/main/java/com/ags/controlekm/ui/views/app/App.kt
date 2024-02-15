package com.ags.controlekm.ui.views.app

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import com.ags.controlekm.R
import com.ags.controlekm.ui.views.serviceManager.components.EmailVerifieldDialog
import com.ags.controlekm.viewModels.CurrentUserViewModel
import com.ags.controlekm.navigation.navigateSingleTopTo
import com.ags.controlekm.navigation.NavHostNavigation
import com.ags.controlekm.ui.views.app.fragments.BottomBar
import com.ags.controlekm.ui.views.app.fragments.BottomSheet
import com.ags.controlekm.ui.views.app.fragments.NavigationDrawer
import com.ags.controlekm.ui.views.app.fragments.TopBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@Composable
fun App(currentUserViewModel: CurrentUserViewModel = hiltViewModel()) {

    val navController = rememberNavController()

    val userLoggedData by currentUserViewModel.currentUser.collectAsState(null)

    val currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val itemsVisible by rememberSaveable { mutableStateOf(true) }

    var showBottomSheet by remember { mutableStateOf(false) }
    
    var showVerificationEmail = remember { mutableStateOf(true) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val perfilImg = rememberImagePainter(
        data = userLoggedData?.image,
        builder = {
            // You can customize loading and error placeholders if needed
            placeholder(R.drawable.perfil)
            error(R.drawable.perfil)
        }
    )

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerContent = {
            AnimatedVisibility(visible = itemsVisible) {
                NavigationDrawer(navController, drawerState)
            }
            // FIM DA ANIMAÇÃO
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
                //FIM ANIMAÇÃO
            }
        ) { innerPadding ->
            if (currentUser?.isEmailVerified == false && showVerificationEmail.value) {
                EmailVerifieldDialog(onDismissRequest = { showVerificationEmail.value = false })
            } else { NavHostNavigation(innerPadding, navController) }

            if (showBottomSheet) {
                BottomSheet(
                    navController = navController,
                    onDismissRequest = { showBottomSheet = false },
                    onClick = { showBottomSheet = false }
                )
            }
        }
    }
}




