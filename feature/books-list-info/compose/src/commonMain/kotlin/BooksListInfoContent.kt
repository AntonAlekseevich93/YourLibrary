import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import elements.BookSelectorItem
import main_models.books.BookShortVo
import models.BooksListInfoScreenEvents

@Composable
fun BooksListInfoContent(
    bookList: List<BookShortVo>,
    viewModel: BooksListInfoViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = bookList) {
        viewModel.setBookList(bookList)
    }

    val listSize by remember(uiState.bookList.value) { mutableStateOf(uiState.bookList.value.size) }

    uiState.bookList.value.fastForEachIndexed { index, item ->
        BookSelectorItem(
            bookItem = item,
            modifier = Modifier.padding(end = 16.dp),
            onClick = { viewModel.sendEvent(BooksListInfoScreenEvents.OnBookSelected(it)) },
            bookHaveReadingStatusEvent = {},
            maxLinesBookName = 4,
            maxLinesAuthorName = 2
        )
        if (index + 1 != listSize) {
            TabRowDefaults.Divider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.1f)
            )
        }
    }

}