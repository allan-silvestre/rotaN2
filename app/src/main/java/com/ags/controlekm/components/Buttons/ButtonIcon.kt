package com.ags.controlekm.components.Buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ButtonIcon(
    icon: ImageVector,
    padding: Dp = 2.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .padding(padding),
        onClick = {
            onClick()
        }) {
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = icon,
            contentDescription = "",
            tint = color,
        )
    }
}