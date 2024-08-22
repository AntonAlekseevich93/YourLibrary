package book_info

import ApplicationTheme
import BookCreatorAppBar
import BookInfoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import kotlinx.coroutines.launch
import main_models.books.BookShortVo
import models.BookScreenEvents

@Composable
fun BookInfoScreen(
    bookItemId: Long?,
    bookShortVo: BookShortVo?,
) {
    val viewModel = remember { Inject.instance<BookInfoViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val appBarHazeState = remember { HazeState() }
    val scrollState = rememberScrollState()

    val isTransparentAppbar = remember { mutableStateOf(true) }
    var reviewButtonPosition by remember { mutableStateOf(0) }

    val bookName = remember(
        key1 = uiState.bookItem.value,
        key2 = bookShortVo
    ) {
        mutableStateOf(
            bookShortVo?.bookName ?: uiState.bookItem.value?.bookName.orEmpty()
        )
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }.collect { scrollOffset ->
            if (scrollOffset > 220 && isTransparentAppbar.value) {
                isTransparentAppbar.value = false
            } else if (scrollOffset < 220 && !isTransparentAppbar.value) {
                isTransparentAppbar.value = true
            }
        }
    }

    LaunchedEffect(key1 = bookItemId, key2 = bookShortVo) {
        bookItemId?.let {
            viewModel.getBookByLocalId(bookItemId)
        }
        bookShortVo?.let {
            viewModel.setShortBook(it)
        }
    }

    Scaffold(
        topBar = {
            BookCreatorAppBar(
                transparentAppbar = isTransparentAppbar,
                title = "",
                onClose = {
                    viewModel.sendEvent(BookScreenEvents.CloseBookInfoScreen)
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.Transparent)
                .verticalScroll(scrollState).haze(
                    state = appBarHazeState,
                    style = HazeStyle(
                        tint = Color.Black.copy(alpha = .2f),
                        blurRadius = 30.dp,
                    )
                ),
        ) {
            Box(
                modifier = Modifier.padding(bottom = 110.dp)
            ) {
                viewModel.BookInfoScreenContent(
                    uiState = uiState,
                    bookShortVo = bookShortVo,
                    bookName = bookName,
                    reviewButtonPosition = {
                        reviewButtonPosition = it
                    },
                    scrollToReviewButtonListener = {
                        scope.launch {
                            scrollState.animateScrollTo(reviewButtonPosition)
                        }
                    }
                )
            }
        }
    }
}