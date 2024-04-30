package book_editor.elements.book_selector.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun SearchBookError() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().heightIn(min = 240.dp)
    ) {
        Image(
            painter = painterResource(DrawableResource(Drawable.drawable_ic_empty_search)),
            contentDescription = null,
            colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
            modifier = Modifier
                .padding(top = 16.dp)
                .size(128.dp)
        )
        Text(
            text = Strings.search_is_empty,
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}