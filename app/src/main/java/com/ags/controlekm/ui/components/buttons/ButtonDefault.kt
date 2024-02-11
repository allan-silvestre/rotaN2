package com.ags.controlekm.ui.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ButtonDefault(
    text: String,
    enable: Boolean = true,
    topStart: Dp = 6.dp,
    topEnd: Dp = 6.dp,
    bottomStart: Dp = 6.dp,
    bottomEnd: Dp = 6.dp,
    fraction: Float = 1f,
    padding: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(fraction)
            .height(50.dp)
            .padding(padding),
        enabled = enable,
        shape = RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
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