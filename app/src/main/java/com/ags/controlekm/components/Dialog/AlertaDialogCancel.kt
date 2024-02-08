package com.ags.controlekm.components.Dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ags.controlekm.components.Buttons.ButtonText
import com.ags.controlekm.components.Text.TitleText
import org.w3c.dom.Text

@Composable
fun AlertaDialogCancel(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    confirmButton: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { title() },
        confirmButton = {
            Row(
                modifier = Modifier.padding(start = 60.dp, end = 60.dp)
            ) {
                ButtonText(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    "Não"
                ) {
                    onDismissRequest()
                }
                ButtonText(
                    modifier = Modifier,
                    "Sim"
                ) {
                    confirmButton()
                    onDismissRequest()
                }
            }
        }
    )
}