package tooltip_area

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun TooltipIconArea(
    text: String,
    drawableResName: String,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    pointerInnerPadding: Dp = 6.dp,
    pointerIsActiveCardColor: Color = ApplicationTheme.colors.pointerIsActiveCardColor,
    pointerEventBackgroundDisable: Color = ApplicationTheme.colors.cardBackgroundDark, //todo переделать токен цвет)
    iconTint: Color = ApplicationTheme.colors.mainIconsColor,
    isSelected: Boolean = false,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    val offset = remember { mutableStateOf(Offset(0f, 0f)) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val cardBackground = if (isSelected) {
        Color(0xFF4D4D50) //todo переделать токен цвет)
    } else if (isHovered.value) {
        pointerIsActiveCardColor
    } else {
        pointerEventBackgroundDisable
    }

    LaunchedEffect(isHovered.value) {
        val tooltip = TooltipItem(
            text = text,
            offset = offset.value,
            showTooltip = isHovered.value
        )
        tooltipCallback?.invoke(tooltip)
    }

    Card(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                offset.value = coordinates.positionInWindow()
            }
            .clickable { onClick.invoke() }
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(cardBackground)
    ) {
        Image(
            painter = painterResource(drawableResName),
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconTint),
            modifier = imageModifier
                .padding(pointerInnerPadding)
                .size(iconSize)
        )
    }
}

@Composable
fun ShowTooltip(
    tooltip: TooltipItem,
    backgroundColor: Color = ApplicationTheme.colors.tooltipAreaBackground,
) {
    val offsetShiftX = when (tooltip.position) {
        TooltipPosition.TOP -> 0
        TooltipPosition.BOTTOM -> 0
        TooltipPosition.LEFT -> 0
        TooltipPosition.RIGHT -> 80
        TooltipPosition.NONE -> 0
    }

    val offsetShiftY = when (tooltip.position) {
        TooltipPosition.TOP -> 0
        TooltipPosition.BOTTOM -> 20
        TooltipPosition.LEFT -> 0
        TooltipPosition.RIGHT -> 100
        TooltipPosition.NONE -> 0
    }
    Card(
        modifier = Modifier
            .layout { measurable, _constraints ->
                val placeable = measurable.measure(Constraints())
                layout(placeable.width, placeable.height) {
                    placeable.place(
                        tooltip.offset.x.toInt() + offsetShiftX,
                        tooltip.offset.y.toInt() - offsetShiftY
                    )
                }
            }
            .background(ApplicationTheme.colors.tooltipAreaBackground),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        androidx.compose.material3.Text(
            text = tooltip.text,
            modifier = Modifier.padding(10.dp),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor
        )
    }
}

enum class TooltipPosition {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM,
    NONE
}

class TooltipItem(
    val text: String = "",
    val offset: Offset = Offset(0f, 0f),
    val showTooltip: Boolean = false,
    var position: TooltipPosition = TooltipPosition.NONE
)

