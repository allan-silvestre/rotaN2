package com.ags.controlekm.components.Text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContentText(text: String) {
    Text(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        text = text,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
}