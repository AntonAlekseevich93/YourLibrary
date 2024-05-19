package book_editor.elements.authors_selector

import ApplicationTheme
import BaseEvent
import BaseEventScope
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import book_editor.BookEditorEvents
import book_editor.elements.CreateBookButton
import containters.CenterBoxContainer
import error.SearchError
import loader.LoadingProcessWithTitle
import main_models.AuthorVo
import main_models.BookValues
import text_fields.DropdownSuggestionItem

@Composable
fun BaseEventScope<BaseEvent>.AuthorsListSelector(
    similarSearchAuthors: SnapshotStateList<AuthorVo>,
    isSearchAuthorProcess: Boolean,
    showError: Boolean,
    bookValues: BookValues,
    maxHeight: Dp = 700.dp
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
                    onClick = { sendEvent(BookEditorEvents.OnCreateBookManually(setCreateNewAuthor = true)) }
                )
            }
        } else {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ApplicationTheme.colors.dropdownBackground
                ),
                modifier = Modifier.sizeIn(maxHeight = maxHeight)
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
                                bookValues.setSelectedAuthorName(
                                    author.name,
                                    relatedAuthorsNames = textPostfix
                                )
                                sendEvent(
                                    BookEditorEvents.OnSuggestionAuthorClickEvent(
                                        author
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}