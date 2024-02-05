package com.ags.controlekm.components.Buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ButtonDefault(
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 2.dp,
                start = 6.dp,
                end = 6.dp
            ),
        shape = RoundedCornerShape(
            topStart = 5.dp,
            topEnd = 5.dp,
            bottomStart = 5.dp,
            bottomEnd = 5.dp
        ),
        onClick = {
            onClick()
        }) {
        Text(
            modifier = Modifier,
            text = text,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}