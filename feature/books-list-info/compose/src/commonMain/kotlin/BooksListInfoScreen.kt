import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import elements.BookSelectorItem
import kotlinx.coroutines.launch
import models.BooksListInfoScreenEvents
import navigation.screen_components.BooksListInfoScreenComponent

@Composable
fun BooksListInfoScreen(
    hazeState: HazeState,
    isHazeBlurEnabled: Boolean,
    navigationComponent: BooksListInfoScreenComponent,
) {
    val viewModel = remember { Inject.instance<BooksListInfoViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        viewModel.setBookList(navigationComponent.books)
    }

    Scaffold(
        topBar = {
            BooksListInfoAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = isHazeBlurEnabled,
                title = navigationComponent.screenTitle,
                showBackButton = true,
                onBack = {
                    navigationComponent.onBack()
                },
                onClose = {
                    navigationComponent.onCloseScreen()
                }
            )
        },
        floatingActionButton = {
            if (lazyListState.firstVisibleItemIndex > 5) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 16.dp),
                    containerColor = ApplicationTheme.colors.mainBackgroundColor,
                    onClick = {
                        scope.launch {
                            lazyListState.scrollToItem(0)
                        }

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowCircleUp,
                        contentDescription = null,
                        tint = ApplicationTheme.colors.mainIconsColor,
                    )
                }
            }
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
        snackbarHost = {

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(top = 1.dp) //fixes haze bug
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = if (isHazeBlurEnabled) {
                    Modifier.haze(
                        state = hazeState,
                        style = HazeStyle(
                            tint = Color.Black.copy(alpha = .04f),
                            blurRadius = 30.dp,
                        )
                    )
                } else {
                    Modifier
                }
            ) {
                item {
                    Spacer(modifier = Modifier.padding(paddingValues.calculateTopPadding()))
                }
                items(uiState.bookList) {
                    BookSelectorItem(
                        bookItem = it,
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            viewModel.sendEvent(
                                BooksListInfoScreenEvents.OnBookSelected(
                                    it,
                                    needSaveScreenId = false
                                )
                            )
                        },
                        maxLinesBookName = 2,
                        maxLinesAuthorName = 1,
                        changeBookReadingStatus = {
//                            changeBookReadingStatus
                        },
                    )
                    Spacer(Modifier.padding(vertical = 12.dp))
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues.calculateBottomPadding()))
                }
            }
        }
    }
}