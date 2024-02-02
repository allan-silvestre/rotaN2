package com.ags.controlekm.functions

import androidx.navigation.NavHostController

fun reiniciarTela(route: String, navController: NavHostController) {
    navController.popBackStack()
    navController.navigateSingleTopTo(route)
}