package elements

import ApplicationTheme
import BaseEvent
import BaseEventScope
import BooksListInfoViewModel
import Strings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import book_editor.elements.BookEditorEvents
import elements.items.AuthorsWithBooksSearchError
import kotlinx.coroutines.launch
import loader.LoadingProcessWithTitle
import main_models.AuthorVo
import main_models.BookValues
import main_models.books.BookShortVo
import models.BookCreatorEvents
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
    openBookInfo: (book: BookShortVo) -> Unit,
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
                        } else if (similarSearchAuthors.isNotEmpty()) {
                            sendEvent(BookCreatorEvents.ClearAuthorSearch)
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
                        if (bookValues.bookName.value.text.isNotEmpty()) {
                            sendEvent(BookCreatorEvents.ClearBooksSearch)
                        }
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
                hazeModifier = hazeModifier,
                onClickManually = onClickManually
            )
        }

        item {
            if (isLoading) {
                LoadingProcessWithTitle(text = Strings.loading_book_search_info)
            }
        }

        items(uiState.bookList) {
            BookSelectorItem(
                bookItem = it,
                modifier = Modifier.padding(end = 16.dp),
                onClick = { selectedBook ->
                    openBookInfo(selectedBook)
                },
                maxLinesBookName = 2,
                maxLinesAuthorName = 1,
                changeBookReadingStatus = changeBookReadingStatus,
                hazeModifier = hazeModifier
            )
            Spacer(Modifier.padding(vertical = 12.dp))
        }

        item {
            if (similarBooks.isNotEmpty()) {
                Column(hazeModifier) {
                    Column(
                        modifier = Modifier.padding(top = 36.dp, bottom = 36.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Не нашли что искали?",
                            style = ApplicationTheme.typography.title3Bold,
                            color = ApplicationTheme.colors.screenColor.activeButtonColor,
                            modifier = Modifier.padding(horizontal = 16.dp).clickable(
                                MutableInteractionSource(), null
                            ) {
                                onClickManually()
                            },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        item {
            if (showError) {
                AuthorsWithBooksSearchError(
                    bookName = bookValues.bookName.value.text,
                    authorName = bookValues.authorName.value.text,
                    onClickManually = onClickManually
                )
            }
        }
        item {
            Spacer(Modifier.padding(bottomPadding).padding(30.dp))
        }
    }
}