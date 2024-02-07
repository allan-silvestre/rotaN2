package com.ags.controlekm.components.Cards

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.HomeMini
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ags.controlekm.components.Text.ContentText
import com.ags.controlekm.components.Text.SubTitleText
import com.ags.controlekm.components.Text.TitleText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AtendimentoCard(
    data: String,
    local: String,
    kmRodado: String,
    status: String
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .width(220.dp)
            .height(120.dp)
            .padding(8.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 30.dp,
                topEnd = 30.dp,
                bottomStart = 30.dp,
                bottomEnd = 30.dp
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                //containerColor = Color(0xFF0B1F3C),
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (data.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "",
                        tint = Color(0xFF228B22)
                    )
                    SubTitleText("${data}")
                    ContentText("${local}")
                } else {
                    SubTitleText("Em andamento")
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(start = 50.dp, end = 50.dp, top = 6.dp, bottom = 6.dp)
                    )
                    ContentText("${local}")
                }
            }
        }
    }
}