package com.ags.controlekm.ui.views.news.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class NewsCardItem(
    val title: String,
    val content: String,
    val profileImage: Painter,
    val profileName: String,
    val ProfileCargo: String,
    val dataPostagem: String,
    val color: Color
)