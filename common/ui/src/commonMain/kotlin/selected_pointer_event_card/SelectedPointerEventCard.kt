package selected_pointer_event_card

import ApplicationTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SelectedPointerEventCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    pointerEventBackgroundDisable: Color = ApplicationTheme.colors.cardBackgroundDark, //todo переделать токен цвет
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val onPointerEventIsActive = remember(PointerEventType) { mutableStateOf(false) }
    val cardBackground = if (onPointerEventIsActive.value) {
        Color(0xFF4D4D50) //todo переделать токен цвет)
    } else {
        pointerEventBackgroundDisable
    }

    Card(
//        modifier =
//        modifier.onClick(onClick = onClick)
//            .onPointerEvent(PointerEventType.Enter) {
//                onPointerEventIsActive.value = true
//            }
//            .onPointerEvent(PointerEventType.Exit) {
//                onPointerEventIsActive.value = false
//            },

        shape = shape,
        colors = CardDefaults.cardColors(cardBackground)
    ) {
        content.invoke()
    }
}