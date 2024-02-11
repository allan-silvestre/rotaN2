package com.ags.controlekm.ui.components.buttons

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ags.controlekm.ui.components.text.SubTitleText

@Composable
fun ButtonText(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
    ) {
        SubTitleText(text)
    }
}