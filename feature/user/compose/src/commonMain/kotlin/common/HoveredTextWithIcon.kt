package common

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HoveredTextWithIcon(
    iconRes: String,
    title: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val cardBackground = if (isHovered.value) {
        ApplicationTheme.colors.pointerIsActiveCardColor
    } else {
        Color.Transparent
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 0.dp)
            .fillMaxWidth()
            .clickable(interactionSource, null) {
                onClick.invoke()
            }
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(cardBackground)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                modifier = Modifier.size(44.dp).padding(end = 6.dp, start = 12.dp)
            )
            Text(
                text = title,
                style = ApplicationTheme.typography.headlineRegular,
                color = ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.padding()
            )
        }
    }
}