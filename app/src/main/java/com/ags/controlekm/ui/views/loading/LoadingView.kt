package com.ags.controlekm.ui.views.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController

@Composable
fun LoadingView() {
    Dialog(onDismissRequest = {}) {
        LinearProgressIndicator(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp),
            strokeCap = StrokeCap.Round
        )
    }
}