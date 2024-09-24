package book_editor.elements.authors_selector

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import book_editor.elements.BookEditorEvents
import book_editor.elements.CreateBookButton
import containters.CenterBoxContainer
import error.SearchError
import loader.LoadingProcessWithTitle
import main_models.AuthorVo
import main_models.BookValues
import text_fields.DropdownSuggestionItem

@Composable
fun BaseEventScope<BaseEvent>.AuthorsListSelector(
    similarSearchAuthors: List<AuthorVo>,
    isSearchAuthorProcess: Boolean,
    showError: Boolean,
    bookValues: BookValues,
    hazeModifier: Modifier = Modifier,
) {
    Column(hazeModifier) {
        AnimatedVisibility(isSearchAuthorProcess) {
            LoadingProcessWithTitle(
                text = "Ищем по всему интернету...",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (showError) {
            SearchError(
                titleAnnotationString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = ApplicationTheme.colors.mainTextColor)) {
                        append(Strings.search_is_empty)
                    }
                    if (bookValues.authorName.value.text.isNotEmpty()) {
                        withStyle(style = SpanStyle(color = ApplicationTheme.colors.titleColors.booksTitleInfoColor)) {
                            append("\n")
                            append(bookValues.authorName.value.text)
                        }
                    }
                    if (bookValues.bookName.value.text.isNotEmpty()) {
                        withStyle(style = SpanStyle(color = ApplicationTheme.colors.titleColors.secondaryBooksTitleInfoColor)) {
                            append("\n")
                            append(bookValues.bookName.value.text)
                        }
                    }
                },
                title = null
            )

            CenterBoxContainer {
                CreateBookButton(
                    title = "Создать новую книгу",
                    modifier = Modifier.padding(top = 24.dp),
                    onClick = {
                        //todo
                    }
                )
            }
        } else {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ApplicationTheme.colors.dropdownBackground
                ),
            ) {
                similarSearchAuthors.fastForEach { author ->
                    DropdownSuggestionItem(
                        text = author.name,
                        itemClickListener = {
                            bookValues.setSelectedAuthorName(
                                author.name,
                                relatedAuthorsNames = author.name
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