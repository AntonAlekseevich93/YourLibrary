package main_models

import androidx.compose.ui.geometry.Offset

class TooltipItem(
    val text: String = "",
    val offset: Offset = Offset(0f, 0f),
    var showTooltip: Boolean = false,
    var position: TooltipPosition = TooltipPosition.NONE
)

enum class TooltipPosition {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM,
    BOTTOM_LEFT,
    NONE
}