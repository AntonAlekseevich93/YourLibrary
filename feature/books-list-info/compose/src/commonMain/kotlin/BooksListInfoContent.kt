import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import elements.BookSelectorItem
import main_models.books.BookShortVo
import models.BooksListInfoScreenEvents

@Composable
fun BooksListInfoContent(
    bookList: List<BookShortVo>,
    viewModel: BooksListInfoViewModel,
    changeBookReadingStatus: (bookId: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = bookList) {
        viewModel.setBookList(bookList)
    }

    Column {
        LazyColumn() {
            items(bookList) {
                BookSelectorItem(
                    bookItem = it,
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = { viewModel.sendEvent(BooksListInfoScreenEvents.OnBookSelected(it)) },
                    maxLinesBookName = 2,
                    maxLinesAuthorName = 1,
                    changeBookReadingStatus = changeBookReadingStatus,
                )
                Spacer(Modifier.padding(vertical = 12.dp))
            }
        }
    }

}