package com.ags.controlekm.ui.views.models

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title: String,
    val navHostLink: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)