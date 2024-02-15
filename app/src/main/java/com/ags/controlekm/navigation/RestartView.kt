package com.ags.controlekm.navigation

import androidx.navigation.NavHostController

fun restartView(route: String, navController: NavHostController) {
    navController.popBackStack()
    navController.navigateSingleTopTo(route)
}