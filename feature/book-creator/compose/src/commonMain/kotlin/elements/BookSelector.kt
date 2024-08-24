package elements

import ApplicationTheme
import BooksListInfoContent
import BooksListInfoViewModel
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import book_editor.elements.CreateBookButton
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
    booksListInfoViewModel: BooksListInfoViewModel,
    onClickManually: () -> Unit,
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

            BooksListInfoContent(
                bookList = similarBooks,
                viewModel = booksListInfoViewModel
            )

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