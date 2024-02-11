package com.ags.controlekm.ui.components.progress

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingCircular() {
    CircularProgressIndicator(
        modifier = Modifier.padding(16.dp),
    )
}