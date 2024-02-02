package com.ags.controlekm.database.Models

import androidx.compose.ui.graphics.Color

data class GraficoDados(
    val color:Color,
    val value:Int,
    val description:String,
    val isTapped:Boolean = false
)
