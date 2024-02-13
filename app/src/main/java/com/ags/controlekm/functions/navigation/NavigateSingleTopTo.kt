package com.ags.controlekm.functions.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }
