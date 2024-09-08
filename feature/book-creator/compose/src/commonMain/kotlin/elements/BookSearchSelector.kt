package elements

import ApplicationTheme
import BaseEvent
import BaseEventScope
import BooksListInfoViewModel
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import book_editor.elements.BookEditorEvents
import book_editor.elements.CreateBookButton
import book_editor.elements.authors_selector.AuthorsListSelector
import containters.CenterBoxContainer
import error.SearchError
import kotlinx.coroutines.launch
import loader.LoadingProcessWithTitle
import main_models.AuthorVo
import main_models.BookValues
import main_models.books.BookShortVo
import models.BooksListInfoScreenEvents
import platform.Platform
import text_fields.SearchTextField
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_authors
import yourlibrary.common.resources.generated.resources.ic_book

@Composable
fun BaseEventScope<BaseEvent>.BookSearchSelector(
    similarBooks: List<BookShortVo>,
    similarSearchAuthors: List<AuthorVo>,
    isLoading: Boolean,
    showError: Boolean,
    hazeModifier: Modifier,
    topPadding: Dp,
    bottomPadding: Dp,
    bookValues: BookValues,
    selectedAuthor: AuthorVo?,
    showSearchBookError: Boolean,
    showSearchAuthorError: Boolean,
    isSearchAuthorProcess: Boolean,
    booksListInfoViewModel: BooksListInfoViewModel,
    lazyListState: LazyListState,
    onClickManually: () -> Unit,
    changeBookReadingStatus: (bookId: String) -> Unit,
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by booksListInfoViewModel.uiState.collectAsState()
    val authorIsSelected by remember(key1 = selectedAuthor) { mutableStateOf(selectedAuthor != null) }
    var lastSearchBookName by remember { mutableStateOf("") }

    LaunchedEffect(key1 = similarBooks) {
        booksListInfoViewModel.setBookList(similarBooks)
    }

    LaunchedEffect(similarBooks) {
        scope.launch {
            state.scrollToItem(0)
        }
    }

    LazyColumn(
        state = lazyListState
    ) {
        item {
            Column(hazeModifier.padding(top = topPadding)) {
                Spacer(modifier = Modifier.padding(top = 12.dp))
                SearchTextField(
                    hintText = if (selectedAuthor != null)
                        "${Strings.search_in_author_books} ${selectedAuthor.name}"
                    else Strings.search_by_book_name,
                    textFieldValue = bookValues.bookName,
                    onTextChanged = {
                        val oldText = bookValues.bookName.value.text
                        if (it.text.trim().isEmpty()) {
                            sendEvent(BookEditorEvents.ClearBookSearch)
                        } else if (authorIsSelected) {
                            sendEvent(BookEditorEvents.OnBookNameChanged(it.text))
                        } else if (showSearchBookError && oldText != it.text) {
                            sendEvent(BookEditorEvents.HideSearchError)
                        }

                        bookValues.bookName.value = it
                    },
                    onClickSearch = {
                        keyboardController?.hide()
                        val newSearch = bookValues.bookName.value.text
                        if (lastSearchBookName != newSearch) {
                            lastSearchBookName = newSearch
                            sendEvent(BookEditorEvents.OnBookNameChanged(newSearch))
                        }
                    },
                    iconResName = Res.drawable.ic_book,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )

                SearchTextField(
                    hintText = Strings.search_by_author,
                    textFieldValue = bookValues.authorName,
                    onTextChanged = {
                        val oldText = bookValues.authorName.value.text
                        bookValues.authorName.value = it

                        sendEvent(
                            BookEditorEvents.OnAuthorTextChanged(
                                textFieldValue = it,
                                textWasChanged = oldText != it.text
                            )
                        )
                    },
                    onClickSearch = {
                        keyboardController?.hide()
                        sendEvent(BookEditorEvents.OnSearchAuthorClick(bookValues.authorName.value.text))
                    },
                    iconResName = Res.drawable.ic_authors,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }

        item {
            AuthorsListSelector(
                isSearchAuthorProcess = isSearchAuthorProcess,
                showError = showSearchAuthorError,
                similarSearchAuthors = similarSearchAuthors,
                bookValues = bookValues,
                hazeModifier = hazeModifier
            )
        }

        item {
            if (isLoading) {
                LoadingProcessWithTitle(text = Strings.loading_book_search_info)
            }
        }

        item {
            if (similarBooks.isNotEmpty()) {
                Column(hazeModifier) {
                    CenterBoxContainer {
                        CreateBookButton(
                            title = "Нет нужной книги",
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            onClickManually()
                        }
                    }

                    CenterBoxContainer {
                        Text(
                            text = Strings.searching_title_result.uppercase(),
                            style = ApplicationTheme.typography.bodyBold,
                            color = ApplicationTheme.colors.mainTextColor,
                            modifier = Modifier.padding(bottom = 26.dp)
                        )
                    }
                }
            }
        }

        items(uiState.bookList) {
            BookSelectorItem(
                bookItem = it,
                modifier = Modifier.padding(end = 16.dp),
                onClick = { selectedBook ->
                    booksListInfoViewModel.sendEvent(
                        BooksListInfoScreenEvents.OnBookSelected(selectedBook, needSaveScreenId = true)
                    )
                },
                maxLinesBookName = 2,
                maxLinesAuthorName = 1,
                changeBookReadingStatus = changeBookReadingStatus,
                hazeModifier = hazeModifier
            )
            Spacer(Modifier.padding(vertical = 12.dp))
        }

        item {
            if (showError) {
                SearchError(
                    titleAnnotationString = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ApplicationTheme.colors.mainTextColor)) {
                            append(Strings.search_is_empty)
                            append(" ")
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
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        onClickManually()
                    }
                }
            }
        }
        item {
            Spacer(Modifier.padding(bottomPadding).padding(30.dp))
        }
    }
}