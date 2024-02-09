package com.ags.controlekm.components.Cards

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ags.controlekm.database.Models.NewsCardItem

@Composable
fun NewsCard(cardItem: NewsCardItem, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {

            }
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = cardItem.profileImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = cardItem.profileName,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = cardItem.ProfileCargo,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                        )
                        Text(
                            text = cardItem.dataPostagem,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                        )

                    }
                    IconButton(
                        onClick = {
                            expanded = !expanded
                        }){
                        Icon(
                            imageVector = if(expanded) Icons.Filled.ExpandLess
                                          else Icons.Filled.ExpandMore,
                            contentDescription = "")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = cardItem.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    modifier = modifier
                        .animateContentSize(
                            animationSpec = tween(durationMillis = 400)
                        ),
                    text = cardItem.content,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = if(expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}