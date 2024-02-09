package com.ags.controlekm.functions

import androidx.navigation.NavHostController

fun restartView(route: String, navController: NavHostController) {
    navController.popBackStack()
    navController.navigateSingleTopTo(route)
}