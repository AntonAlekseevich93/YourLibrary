package items

import ApplicationTheme
import Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import models.SearchTaskItemUiState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import selected_pointer_event_card.SelectedPointerEventCard

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun SearchedTask(
    task: SearchTaskItemUiState,
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SelectedPointerEventCard(
        onClick = onClick,
        modifier = modifier,
        pointerEventBackgroundDisable = ApplicationTheme.colors.searchBackground
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = innerModifier
        ) {
            Image(
                painter = painterResource(Drawable.drawable_ic_task),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                modifier = Modifier.size(24.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f, fill = true)
            ) {
                Text(
                    text = task.name,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 18.dp, end = 16.dp)
                )
                Text(
                    text = task.description,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.searchDescriptionTextColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 18.dp, end = 16.dp)
                )
            }
        }
    }
}