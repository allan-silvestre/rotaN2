package com.ags.controlekm.ui.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ags.controlekm.ui.views.addressManager.AllAddressView
import com.ags.controlekm.ui.views.login.ForgotPasswordView
import com.ags.controlekm.ui.views.serviceManager.ServiceManagerView
import com.ags.controlekm.ui.views.login.LoginView
import com.ags.controlekm.ui.views.login.RegisterUserView
import com.ags.controlekm.ui.views.news.NewsView

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
            ServiceManagerView(navController)
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