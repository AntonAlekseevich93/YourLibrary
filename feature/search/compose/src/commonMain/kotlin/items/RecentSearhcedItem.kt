package items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import selected_pointer_event_card.SelectedPointerEventCard

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun RecentSearchedItem(
    text: String,
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    onClick: (text: String) -> Unit,
) {
    SelectedPointerEventCard(
        onClick = { onClick.invoke(text) },
        modifier = modifier,
        pointerEventBackgroundDisable = ApplicationTheme.colors.searchBackground
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = innerModifier
        ) {
            Image(
                painter = painterResource(Drawable.drawable_ic_search_glass),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 18.dp)
            )
            Spacer(modifier = Modifier.weight(1f, fill = true))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = ApplicationTheme.colors.searchIconColor,
                modifier = Modifier.padding(end = 16.dp).size(18.dp)
            )
        }
    }
}