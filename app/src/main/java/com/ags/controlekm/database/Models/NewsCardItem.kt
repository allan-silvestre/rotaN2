package com.ags.controlekm.database.Models

import android.media.Image
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