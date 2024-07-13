package book_editor.elements.book_selector

import ApplicationTheme
import Strings
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import book_editor.elements.CreateBookButton
import book_editor.elements.book_selector.elements.BookSelectorItem
import containters.CenterBoxContainer
import error.SearchError
import kotlinx.coroutines.launch
import loader.LoadingProcessWithTitle
import main_models.BookValues
import main_models.books.BookShortVo
import org.jetbrains.compose.resources.painterResource
import platform.Platform
import platform.isDesktop
import platform.isMobile
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_arrow_right_in_round

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
            Box {
                LazyRow(state = state) {
                    items(similarBooks) {
                        BookSelectorItem(
                            bookItem = it,
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = onClick,
                            bookHaveReadingStatusEvent = bookHaveReadingStatusEvent
                        )
                    }
                }
                if (showAllBooksButton) {
                    Card(
                        modifier = Modifier.height(200.dp).align(Alignment.TopEnd),
                        colors = CardDefaults.cardColors(
                            containerColor = ApplicationTheme.colors.mainBackgroundColor.copy(
                                alpha = 0.9f
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                    ) {
                        val padding = if (platform.isMobile()) 4.dp else 22.dp
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.height(200.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = showAllBooksListener
                            )
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ic_arrow_right_in_round),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(ApplicationTheme.colors.screenColor.iconColor),
                                modifier = Modifier.padding(horizontal = padding)
                                    .size(34.dp),
                            )
                        }
                    }
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