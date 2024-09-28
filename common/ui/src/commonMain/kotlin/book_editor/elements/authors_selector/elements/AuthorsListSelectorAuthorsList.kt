package book_editor.elements.authors_selector.elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import main_models.AuthorVo

@Composable
internal fun AuthorsListSelectorAuthorsList(
    authors: List<AuthorVo>,
    authorClickListener: (AuthorVo) -> Unit,
    createBookManuallyListener: () -> Unit,
) {
    Column(modifier = Modifier.padding(end = 16.dp, start = 18.dp)) {
        authors.fastForEach { author ->
            AuthorsListSelectorAuthorItem(author) {
                authorClickListener(author)
            }
        }
        if (authors.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = 36.dp, bottom = 36.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Нет нужного автора?",
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.screenColor.activeButtonColor,
                    modifier = Modifier.padding(horizontal = 16.dp).clickable(
                        MutableInteractionSource(), null
                    ) {
                        createBookManuallyListener()
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

