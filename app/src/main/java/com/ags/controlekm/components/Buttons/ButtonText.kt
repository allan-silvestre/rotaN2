package com.ags.controlekm.components.Buttons

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ags.controlekm.components.Text.SubTitleText

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