package profile.elements

import ApplicationTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AvatarWithName(name: String) {
    val avatarColor = Color(0xFF8ac926)
    val strokeColor = ApplicationTheme.colors.mainIconsColor.copy(alpha = 0.5f)
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(86.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2
                drawCircle(
                    color = avatarColor,
                    radius = radius - 5.dp.toPx()
                )
                drawCircle(
                    color = Color.Transparent,
                    radius = radius,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = strokeColor,
                    radius = radius - 2.dp.toPx(),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            if (name.isNotEmpty()) {
                Text(
                    text = name.firstOrNull().toString(),
                    style = ApplicationTheme.typography.title1Bold.copy(fontSize = 32.sp),
                    color = ApplicationTheme.colors.mainTextColor
                )
            }
        }

        Text(
            text = name,
            style = ApplicationTheme.typography.title2Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
            color = ApplicationTheme.colors.mainTextColor
        )
    }
}