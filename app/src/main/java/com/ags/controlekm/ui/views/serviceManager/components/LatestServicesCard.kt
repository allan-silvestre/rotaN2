package com.ags.controlekm.ui.views.serviceManager.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.ags.controlekm.ui.components.text.ContentText
import com.ags.controlekm.ui.components.text.SubTitleText

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LatestServicesCard(
    data: String,
    address: String,
) {
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
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "",
                        tint = Color(0xFF228B22)
                    )
                    SubTitleText(data)
                    ContentText(address)
                } else {
                    SubTitleText("Em andamento")
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(start = 50.dp, end = 50.dp, top = 6.dp, bottom = 6.dp),
                        strokeCap = StrokeCap.Round
                    )
                    ContentText(address)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .clickable {  },
                imageVector = Icons.Rounded.OpenInNew,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }
}