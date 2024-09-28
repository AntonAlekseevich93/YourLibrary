package book_editor.elements.authors_selector.elements

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import main_models.AuthorVo
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_authors

@Composable
internal fun AuthorsListSelectorAuthorItem(
    author: AuthorVo,
    onAuthorClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 12.dp, end = 16.dp)
            .clickable(MutableInteractionSource(), null) {
                onAuthorClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_authors),
            contentDescription = null,
            colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
            modifier = Modifier.padding(end = 12.dp).size(18.dp)
        )

        Text(
            text = author.name,
            style = ApplicationTheme.typography.headlineRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(end = 16.dp, top = 12.dp, bottom = 12.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}