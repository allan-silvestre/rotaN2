package com.ags.controlekm.ui.views.loading

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.ags.controlekm.ui.components.text.ContentText
import com.ags.controlekm.ui.components.text.SubTitleText

@Composable
fun Shimmer() {
    val colors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
    ShimmerGridItem(brush)
}

@Composable
fun ShimmerGridItem(brush: Brush) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(brush),
        )
        Spacer(
            modifier = Modifier
                .width(150.dp)
                .height(30.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(brush),
        )
        Row {
            repeat(2){
                Spacer(
                    modifier = Modifier
                        .width(220.dp)
                        .height(110.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(brush),
                )
            }

        }
    }
}