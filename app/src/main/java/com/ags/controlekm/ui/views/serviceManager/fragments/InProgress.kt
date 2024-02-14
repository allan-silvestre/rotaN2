package com.ags.controlekm.ui.views.serviceManager.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ags.controlekm.ui.components.text.ContentText
import com.ags.controlekm.ui.components.text.TitleText
import com.ags.controlekm.ui.components.textField.FormularioOutlinedTextField

@Composable
fun InProgress(
    error: Boolean = true,
    serviceSummary: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText("Atendimento")
        Spacer(modifier = Modifier.height(24.dp))
        ContentText("Em andamento")
        Spacer(modifier = Modifier.height(24.dp))
        FormularioOutlinedTextField(
            readOnly = false,
            value = serviceSummary.toString(),
            onValueChange = { serviceSummary(it.take(50)) },
            label = "Resumo do atendimento",
            visualTransformation = VisualTransformation.None,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Sentences,
            erro = error,
            erroMensagem = ""
        )
    }
}