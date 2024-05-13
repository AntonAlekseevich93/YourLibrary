package book_editor.elements.authors_selector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import book_editor.BookEditor
import book_editor.BookEditorEvents
import book_editor.elements.CreateBookButton
import containters.CenterBoxContainer
import error.SearchError
import loader.LoadingProcessWithTitle
import main_models.AuthorVo
import text_fields.DropdownSuggestionItem

@Composable
fun AuthorsListSelector(
    similarSearchAuthors: SnapshotStateList<AuthorVo>,
    isSearchAuthorProcess: Boolean,
    showError: Boolean,
    itemClickListener: (author: AuthorVo, textPostfix: String) -> Unit,
    onClickManually: () -> Unit,
) {
    Column {
        AnimatedVisibility(isSearchAuthorProcess) {
            LoadingProcessWithTitle(
                text = "Ищем по всему интернету...",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (showError) {
            SearchError()

            CenterBoxContainer {
                CreateBookButton(
                    title = "Создать новую книгу",
                    modifier = Modifier.padding(top = 24.dp),
                    onClick = { onClickManually() }
                )
            }
        } else {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ApplicationTheme.colors.dropdownBackground
                ),
                modifier = Modifier.sizeIn(maxHeight = 140.dp)

            ) {
                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                    items(similarSearchAuthors) { author ->
                        val textPostfix = if (author.relatedAuthors.isNotEmpty()) {
                            "(${author.relatedAuthors.joinToString { it.name }})"
                        } else ""
                        val text = "${author.name}$textPostfix"
                        DropdownSuggestionItem(
                            text = text,
                            itemClickListener = {
                                itemClickListener(author, textPostfix)

                            },
                        )
                    }
                }
            }
        }
    }
}