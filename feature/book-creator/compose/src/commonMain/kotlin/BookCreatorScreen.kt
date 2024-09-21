import alert_dialog.CommonAlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import animations.SuccessAnimation
import book_editor.elements.BookEditorEvents
import date.CommonDatePicker
import date.DatePickerEvents
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import elements.BookSearchSelector
import genre.GenreSelector
import kotlinx.coroutines.launch
import main_models.DatePickerType
import models.BookCreatorEvents
import reading_status.ReadingStatusSelectorDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCreatorScreen(
    hazeState: HazeState,
) {
    val viewModel = remember { Inject.instance<BookCreatorViewModel>() }
    val booksListInfoViewModel = remember { Inject.instance<BooksListInfoViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val dataPickerState = rememberDatePickerState()
    val scope = rememberCoroutineScope()
    var selectionGenreState by remember { mutableStateOf(false) }
    val showSuccessAnimation = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BookCreatorAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = if (uiState.isCreateBookManually || uiState.shortBookItem != null) Strings.add_book else "Поиск",
                showBackButton = uiState.showFullScreenBookSelector,
                showSearchButton = uiState.isCreateBookManually || uiState.shortBookItem != null,
                onClose = {
                }
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
                    viewModel.sendEvent(BookEditorEvents.OnCreateBookManually(bookWasNotFound = true))
                },
                changeBookReadingStatus = {
                    viewModel.sendEvent(
                        BookCreatorEvents.SetSelectedBookByMenuClick(
                            bookId = it,
                        )
                    )
                },
            )
        }

        AnimatedVisibility(uiState.showDatePicker) {
            val timePickerTitle =
                remember { if (uiState.datePickerType == DatePickerType.StartDate) Strings.start_date else Strings.end_date }
            viewModel.CommonDatePicker(
                state = dataPickerState,
                title = timePickerTitle,
                datePickerType = uiState.datePickerType,
                onDismissRequest = {
                    viewModel.sendEvent(DatePickerEvents.OnHideDatePicker)
                },
            )
        }

        AnimatedVisibility(
            visible = uiState.showDialogClearAllData,
            exit = fadeOut(animationSpec = tween(durationMillis = 1))
        ) {
            ClearDataAlertDialog(
                onDismissRequest = {
                    viewModel.sendEvent(BookCreatorEvents.OnShowDialogClearAllData(false))
                }, onClick = {
                    viewModel.sendEvent(BookCreatorEvents.ClearUrlEvent)
                }
            )
        }

        if (uiState.selectedBookByMenuClick.value != null) {
            ReadingStatusSelectorDialog(
                currentStatus = uiState.selectedBookByMenuClick.value?.bookVo?.readingStatus,
                useDivider = false,
                selectStatusListener = {
                    showSuccessAnimation.value = true
                    viewModel.sendEvent(BookCreatorEvents.ChangeBookReadingStatus(it))
                },
                dismiss = { viewModel.sendEvent(BookCreatorEvents.ClearSelectedBook) }
            )
        }

        if (uiState.showCommonAlertDialog) {
            uiState.alertDialogConfig?.let { config ->
                CommonAlertDialog(
                    config = config,
                    acceptListener = {
                        config.acceptEvent?.let { viewModel.sendEvent(it) }
                    },
                    onDismissRequest = {
                        config.dismissEvent?.let { viewModel.sendEvent(it) }
                    }
                )
            }


            AnimatedVisibility(selectionGenreState) {
                BasicAlertDialog(
                    modifier = Modifier.fillMaxSize(),
                    onDismissRequest = { selectionGenreState = false },
                    properties = DialogProperties(usePlatformDefaultWidth = true)
                ) {
                    GenreSelector(
                        Modifier.background(ApplicationTheme.colors.screenColor.background)
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) { genre ->
                        viewModel.sendEvent(BookCreatorEvents.SetSelectedGenre(genre = genre))
                        selectionGenreState = false
                    }
                }
            }
        }

        SuccessAnimation(
            show = showSuccessAnimation,
            finishAnimation = {
                showSuccessAnimation.value = false
            }
        )
    }
}