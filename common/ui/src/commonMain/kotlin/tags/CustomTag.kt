package tags

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTag(
    text: String,
    color: Color = Color.White,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    maxHeight: Dp = 24.dp,
    textStyle: TextStyle = ApplicationTheme.typography.footnoteRegular,
    onClick: () -> Unit,
) {
    Chip(
        shape = shape,
        modifier = modifier.sizeIn(maxHeight = maxHeight),
        border = BorderStroke(1.dp, color = color),
        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = textStyle,
            color = color,
            modifier = textModifier
        )
    }
}