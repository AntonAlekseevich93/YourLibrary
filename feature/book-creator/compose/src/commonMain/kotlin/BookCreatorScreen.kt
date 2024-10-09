import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import animations.ConfettiAnimation
import animations.SuccessAnimation
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import elements.BookSearchSelector
import kotlinx.coroutines.launch
import models.BookCreatorEvents
import navigation.screen_components.BookCreatorScreenComponent
import reading_status.ReadingStatusSelectorDialog


@Composable
fun BookCreatorScreen(
    hazeState: HazeState,
    navigationComponent: BookCreatorScreenComponent,
) {
    val viewModel = remember { Inject.instance<BookCreatorViewModel>() }
    val booksListInfoViewModel = remember { Inject.instance<BooksListInfoViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showSuccessAnimation = remember { mutableStateOf(false) }
    val showConfettiAnimation = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BookCreatorAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = "Поиск",
                showBackButton = false,
                onBack = {}
            )
        },
        floatingActionButton = {
            if (lazyListState.firstVisibleItemIndex > 5) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 76.dp),
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
            SnackbarHost(hostState = uiState.snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp) //fixes haze blur bug
                .background(Color.Transparent)
        ) {
            viewModel.BookSearchSelector(
                lazyListState = lazyListState,
                similarBooks = uiState.similarBooks,
                isLoading = uiState.isSearchBookProcess,
                hazeModifier = Modifier.haze(
                    state = hazeState,
                    style = HazeStyle(
                        tint = Color.Black.copy(alpha = .04f),
                        blurRadius = 30.dp,
                    )
                ),
                topPadding = it.calculateTopPadding(),
                bottomPadding = it.calculateBottomPadding(),
                showError = uiState.showSearchBookError,
                bookValues = uiState.bookValues,
                booksListInfoViewModel = booksListInfoViewModel,
                selectedAuthor = uiState.selectedAuthor,
                similarSearchAuthors = uiState.similarSearchAuthors,
                isSearchAuthorProcess = uiState.isSearchAuthorProcess,
                showSearchAuthorError = uiState.showSearchAuthorError,
                showSearchBookError = uiState.showSearchBookError,
                onClickManually = {
                    uiState.updateUserBookCreatorUiState()
                    viewModel.sendEvent(BookCreatorEvents.CheckIfAuthorIsMatchingAndSetOnCreatedUserScreen)
                    navigationComponent.openUserBookCreatorScreen()
                },
                changeBookReadingStatus = {
                    viewModel.sendEvent(
                        BookCreatorEvents.SetSelectedBookByMenuClick(
                            bookId = it,
                        )
                    )
                },
                openBookInfo = { shortBook ->
                    navigationComponent.openBookInfo(
                        bookId = null,
                        shortBook = shortBook,
                        needSaveScreenId = true
                    )
                }
            )
        }

        if (uiState.selectedBookByMenuClick.value != null) {
            showSuccessAnimation.value = false
            showConfettiAnimation.value = false
            ReadingStatusSelectorDialog(
                currentStatus = uiState.selectedBookByMenuClick.value?.bookVo?.readingStatus,
                useDivider = false,
                selectStatusListener = {
                    showSuccessAnimation.value = true
                    showConfettiAnimation.value = true
                    viewModel.sendEvent(BookCreatorEvents.ChangeBookReadingStatus(it))
                },
                dismiss = { viewModel.sendEvent(BookCreatorEvents.ClearSelectedBook) }
            )
        }
        SuccessAnimation(
            show = showSuccessAnimation,
            finishAnimation = {
                showSuccessAnimation.value = false
            }
        )
        ConfettiAnimation(
            show = showConfettiAnimation,
            finishAnimation = {
                showConfettiAnimation.value = false
            }
        )
    }
}