package book_editor.book_selector

import ApplicationTheme
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
import book_editor.book_selector.elements.BookSelectorItem
import main_models.books.BookShortVo

@Composable
fun BookSelector(
    similarBooks: SnapshotStateList<BookShortVo>,
    modifier: Modifier = Modifier,
    onClick: (book: BookShortVo) -> Unit,
) {
    val state = rememberLazyListState()
    Column(modifier = modifier) {
        Text(
            text = "Что удалось найти:",
            style = ApplicationTheme.typography.bodyRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            state = state,

            ) {
            items(similarBooks) {
                BookSelectorItem(it, modifier = Modifier.padding(end = 16.dp), onClick)
            }
        }
    }
}