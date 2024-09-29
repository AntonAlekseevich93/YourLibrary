package book_editor.elements.authors_selector

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import book_editor.elements.BookEditorEvents
import book_editor.elements.authors_selector.elements.AuthorsListSelectorAuthorsList
import error.SearchError
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