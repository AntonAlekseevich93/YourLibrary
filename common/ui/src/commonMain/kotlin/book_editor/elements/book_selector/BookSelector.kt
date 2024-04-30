package book_editor.elements.book_selector

import ApplicationTheme
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import book_editor.elements.book_selector.elements.BookSelectorItem
import book_editor.elements.book_selector.elements.LoadingBookProcess
import book_editor.elements.book_selector.elements.SearchBookError
import containters.CenterBoxContainer
import main_models.books.BookShortVo

@Composable
fun BookSearchSelector(
    similarBooks: SnapshotStateList<BookShortVo>,
    isLoading: Boolean,
    showError: Boolean,
    modifier: Modifier = Modifier,
    onClick: (book: BookShortVo) -> Unit,
) {
    val state = rememberLazyListState()
    Column(modifier = modifier) {
        if (isLoading) {
            LoadingBookProcess()
        } else if (similarBooks.isNotEmpty()) {
            CenterBoxContainer {
                Text(
                    text = Strings.searching_title_result.uppercase(),
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(bottom = 26.dp)
                )
            }

            LazyRow(state = state) {
                items(similarBooks) {
                    BookSelectorItem(it, modifier = Modifier.padding(end = 16.dp), onClick)
                }
            }
        } else if (showError) {
            SearchBookError()
        }
    }
}