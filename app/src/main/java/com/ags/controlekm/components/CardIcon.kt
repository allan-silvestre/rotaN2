package com.ags.controlekm.components

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ags.controlekm.objects.CardIconItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardIcon(cardItem: CardIconItem, onClickAction: () -> Unit) {
    Column(
        modifier = Modifier.wrapContentSize(),
            //.size(width = 90.dp, height = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onClickAction() },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
        ) {
            BadgedBox(
                badge = { Badge {
                                if (2 == 2) {
                        Badge {
                            Text(text = "5")
                        }
                    }
                }
            },
            ) {
                Icon(
                    modifier = Modifier
                        .padding(16.dp),
                    imageVector = cardItem.icon,
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = cardItem.title,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 10.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            minLines = 2
        )
    }
    Spacer(modifier = Modifier.width(20.dp))
}