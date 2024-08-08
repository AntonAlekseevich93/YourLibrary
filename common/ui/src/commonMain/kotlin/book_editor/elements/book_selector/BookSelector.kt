package book_editor.elements.book_selector

import ApplicationTheme
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import book_editor.elements.CreateBookButton
import book_editor.elements.book_selector.elements.BookSelectorItem
import containters.CenterBoxContainer
import error.SearchError
import kotlinx.coroutines.launch
import loader.LoadingProcessWithTitle
import main_models.BookValues
import main_models.books.BookShortVo
import platform.Platform
import platform.isDesktop
import platform.isMobile

@Composable
fun BookSearchSelector(
    similarBooks: List<BookShortVo>,
    isLoading: Boolean,
    showError: Boolean,
    modifier: Modifier = Modifier,
    bookValues: BookValues,
    platform: Platform,
    onClick: (book: BookShortVo) -> Unit,
    onClickManually: () -> Unit,
    bookHaveReadingStatusEvent: () -> Unit,
    showAllBooksListener: () -> Unit,
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showAllBooksButton = remember(key1 = similarBooks.size) {
        platform.isDesktop() && similarBooks.size > 5 || platform.isMobile() && similarBooks.size > 2
    }

    LaunchedEffect(similarBooks) {
        scope.launch {
            state.scrollToItem(0)
        }
    }

    Column(modifier = modifier) {
        if (isLoading) {
            LoadingProcessWithTitle(text = Strings.loading_book_search_info)
        } else if (similarBooks.isNotEmpty()) {
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
            val listSize = remember(similarBooks.size) { similarBooks.size }
            similarBooks.fastForEachIndexed { index, item ->
                BookSelectorItem(
                    bookItem = item,
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = onClick,
                    bookHaveReadingStatusEvent = bookHaveReadingStatusEvent,
                    maxLinesBookName = 4,
                    maxLinesAuthorName = 2
                )
                if (index + 1 != listSize) {
                    Divider(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.1f)
                    )
                }
            }
        } else if (showError) {
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
}