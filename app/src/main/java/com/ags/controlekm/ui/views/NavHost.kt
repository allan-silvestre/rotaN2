package com.ags.controlekm.ui.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ags.controlekm.ui.views.home.HomeView
import com.ags.controlekm.ui.views.home.ServiceManagerCard

@Composable
fun NavHostNavigation(
    padding: PaddingValues,
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginView(navController)
        }
        composable("newUser") {
            RegisterUserView(navController)
        }
        composable("forgotPassword") {
            ForgotPasswordView(navController)
        }
        composable("home") {
            HomeView(navController)
        }
        composable("news") {
            NewsView(navController)
        }
        composable("enderecosAtendimento") {
            AllAddressView(navController)
        }
        composable("loading") {
            LoadingView(navController)
        }
    }
}