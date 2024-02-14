package com.ags.controlekm.ui.views.home

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DateAndTime(
    date: (String) -> Unit,
    time: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val handler = remember { Handler(Looper.getMainLooper()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                handler.post {
                    val currentTime = System.currentTimeMillis()

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    date( dateFormat.format(currentTime) )

                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    time( timeFormat.format(currentTime) )
                }
            }
        }
    }
}