package elements

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import main_models.books.BookShortVo
import platform.Platform
import platform.isMobile

//todo maybe need delete

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookListSelector(
    shortBooks: List<BookShortVo>,
    platform: Platform,
    itemClickListener: (book: BookShortVo) -> Unit,
) {
    val scroll = rememberScrollState()
    val resultList = remember(key1 = shortBooks) { shortBooks }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (platform.isMobile()) {
            Column(Modifier.padding(top = 24.dp, bottom = 46.dp)) {
                resultList.fastForEachIndexed { index, item ->
                    if (index % 2 == 0) {
                        val nextItemExist = resultList.getOrNull(index + 1) != null
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding()
                        ) {
                            if (nextItemExist) {
                                Spacer(Modifier.weight(1f))
                            }

                            Item(
                                item = item,
                                onClick = itemClickListener,
                                bookHaveReadingStatusEvent = {}
                            )

                            if (nextItemExist) {
                                Spacer(Modifier.weight(1f))
                                Item(
                                    item = resultList[index + 1],
                                    onClick = itemClickListener,
                                    bookHaveReadingStatusEvent = {}
                                )
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        } else {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                resultList.fastForEach {
                    Item(
                        item = it,
                        onClick = itemClickListener,
                        bookHaveReadingStatusEvent = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    item: BookShortVo,
    onClick: (book: BookShortVo) -> Unit,
    bookHaveReadingStatusEvent: () -> Unit,
) {
    BookSelectorItem(
        bookItem = item,
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
            top = 8.dp,
            bottom = 8.dp
        ),
        maxLinesAuthorName = 2,
        maxLinesBookName = 2,
        onClick = onClick,
        bookHaveReadingStatusEvent = bookHaveReadingStatusEvent,
    )
}
