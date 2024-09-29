package elements

import BaseEvent
import BaseEventScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import book_editor.elements.BookEditorEvents
import book_editor.elements.authors_selector.elements.AuthorsListSelectorAuthorsList
import elements.items.AuthorsWithBooksSearchError
import loader.LoadingProcessWithTitle
import main_models.AuthorVo
import main_models.BookValues
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.searching_in_internet_loading_text
import yourlibrary.common.resources.generated.resources.text_button_author_not_exist

@Composable
fun BaseEventScope<BaseEvent>.AuthorsListSelector(
    similarSearchAuthors: List<AuthorVo>,
    isSearchAuthorProcess: Boolean,
    showError: Boolean,
    bookValues: BookValues,
    hazeModifier: Modifier = Modifier,
    onClickManually: () -> Unit,
) {
    Column(hazeModifier) {
        AnimatedVisibility(isSearchAuthorProcess) {
            LoadingProcessWithTitle(
                text = stringResource(Res.string.searching_in_internet_loading_text),
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (showError) {
            AuthorsWithBooksSearchError(
                bookName = bookValues.bookName.value.text,
                authorName = bookValues.authorName.value.text,
                onClickManually = onClickManually
            )
        } else {
            AuthorsListSelectorAuthorsList(similarSearchAuthors,
                authorClickListener = { author ->
                    bookValues.setSelectedAuthorName(
                        author.name,
                    )
                    sendEvent(
                        BookEditorEvents.OnSuggestionAuthorClickEvent(
                            author
                        )
                    )
                },
                bottomButtonTitleRes = Res.string.text_button_author_not_exist,
                bottomButtonClickListener = {
                    onClickManually()
                }
            )
        }
    }
}