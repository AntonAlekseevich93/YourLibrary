package main_app_bar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBarGradientBox() {
    val colors = listOf(
        Color(0xf4f3ee),
        Color(0xFFbcb8b1),
        Color(0xFF343a40)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val gradientOffset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedBrush = remember(gradientOffset.value) {
        Brush.linearGradient(
            colors = colors,
            start = Offset(gradientOffset.value * 500, 0f),
            end = Offset(
                (gradientOffset.value * 1000 + 500) % 3000,
                0f
            )
        )
    }

    Box(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(animatedBrush)
            .padding(bottom = 45.dp)
    )
}