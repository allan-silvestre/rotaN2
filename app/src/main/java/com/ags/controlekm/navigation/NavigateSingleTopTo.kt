package com.ags.controlekm.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }
