import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import main_models.BookItemVo

/**
 * dont use in this function fillMaxWidth
 * When you move the card, the card will stretch to fill the entire screen
 * Use minWidth
 */
@Composable
fun BookItemCard(
    taskItem: BookItemVo,
    minWidth: Dp,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ApplicationTheme.colors.cardBackgroundLight,
    taskCheckListener: (isChecked: Boolean) -> Unit
) {
}