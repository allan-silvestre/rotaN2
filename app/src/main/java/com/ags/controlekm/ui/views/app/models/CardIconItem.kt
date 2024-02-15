package com.ags.controlekm.ui.views.app.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CardIconItem(
    val title: String,
    val navHostLink: String,
    val icon: ImageVector,
    val color: Color,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)