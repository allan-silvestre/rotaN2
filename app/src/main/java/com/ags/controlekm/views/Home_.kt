package com.ags.controlekm.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ags.controlekm.objects.CardIconItem

@Composable
fun Home_(
    navController: NavHostController
) {// CONTROLAR / SUBSTITUIR EVENTO BOTÃO VOLTAR
    var backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
        // Handle back press
    }
    // ATENDIMENTO CARDITEMS
    val cardItemsAtendimentoOptions = listOf(
        CardIconItem(
            "Novo",
            "newService",
            Icons.Filled.AddLocationAlt,
            MaterialTheme.colorScheme.primary,

            ),
        CardIconItem(
            "Em andamento",
            "",
            Icons.Filled.SwapHorizontalCircle,
            MaterialTheme.colorScheme.secondary,
            badgeCount = 5
        ),
        CardIconItem(
            "Concluídos",
            "",
            Icons.Filled.DoneAll,
            MaterialTheme.colorScheme.secondary
        ),
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .drawBehind {
                    drawRect(
                        color = Color(0xFF005CBB),
                        size = Size(size.width, size.height / 1.7f)
                    )
                },
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .height(250.dp)
                    .padding(start = 12.dp, end = 12.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        text = "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    val dataList = mutableListOf(0, 315, 210, 90, 0, 295, 0)
                    val floatValue = mutableListOf<Float>()
                    val datesList = mutableListOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab")

                    dataList.forEachIndexed { index, value ->

                        floatValue.add(index = index, element = value.toFloat()/dataList.max().toFloat())

                    }
                    Grafico(
                        graphBarData = floatValue,
                        xAxisScaleData = datesList,
                        barData_ = dataList,
                        height = 100.dp,
                        roundType = BarType.TOP_CURVED,
                        barWidth = 30.dp,
                        barColor = MaterialTheme.colorScheme.primary,
                        barArrangement = Arrangement.SpaceEvenly
                    )
                }
            }
        }
    }
}

