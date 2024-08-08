import alert_dialog.CommonAlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import book_editor.BookEditor
import book_list_selector.BookListSelector
import date.CommonDatePicker
import date.DatePickerEvents
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import genre.GenreSelector
import kotlinx.coroutines.launch
import main_models.DatePickerType
import main_models.books.BookShortVo
import models.BookCreatorEvents
import platform.Platform


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCreatorScreen(
    platform: Platform,
    fullScreenBookCreator: MutableState<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    isKeyboardShown: State<Boolean>,
    modifier: Modifier = Modifier,
    hazeState: HazeState,
) {
    val viewModel = remember { Inject.instance<BookCreatorViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollableState = rememberScrollState()

    val statusBookTextFieldValue =
        remember { mutableStateOf(TextFieldValue(text = uiState.defaultStatus.nameValue)) }
    val dataPickerState = rememberDatePickerState()
    val scope = rememberCoroutineScope()
    var selectionGenreState by remember { mutableStateOf(false) }

    var book: BookShortVo? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            BookCreatorAppBar(
                hazeBlurState = hazeState,
                title = if (uiState.isCreateBookManually || uiState.shortBookItem != null) Strings.add_book else "Поиск",
                showBackButton = uiState.showFullScreenBookSelector,
                showSearchButton = uiState.isCreateBookManually || uiState.shortBookItem != null,
                onClose = {
                    viewModel.sendEvent(BookCreatorEvents.GoBack)
                }
            )
        },
        floatingActionButton = {
            if (scrollableState.value > 2000) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 76.dp),
                    containerColor = ApplicationTheme.colors.mainBackgroundColor,
                    onClick = {
                        scope.launch {
                            scrollableState.scrollTo(0)
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
                .verticalScroll(scrollableState)
        ) {
            Spacer(modifier = Modifier.padding(top = 12.dp))
            viewModel.BookEditor(
                modifier = Modifier
                    .haze(
                        state = hazeState,
                        style = HazeStyle(
                            tint = Color.Black.copy(alpha = .2f),
                            blurRadius = 30.dp,
                        )
                    )
                    .padding(top = it.calculateTopPadding()),
                platform = platform,
                bookValues = uiState.bookValues,
                similarSearchAuthors = uiState.similarSearchAuthors,
                selectedAuthor = uiState.selectedAuthor,
                createNewAuthor = uiState.needCreateNewAuthor,
                isKeyboardShown = isKeyboardShown.value,
                statusBookTextFieldValue = statusBookTextFieldValue,
                similarBooks = uiState.similarBooks,
                isSearchBookProcess = uiState.isSearchBookProcess,
                isSearchAuthorProcess = uiState.isSearchAuthorProcess,
                isCreateBookManually = uiState.isCreateBookManually,
                shortBook = uiState.shortBookItem,
                isBookCoverManually = uiState.isBookCoverManually,
                showSearchBookError = uiState.showSearchBookError,
                showSearchAuthorError = uiState.showSearchAuthorError,
                genreSelectorListener = { selectionGenreState = true },
                bookWasNotFound = uiState.bookWasNotFound,
                authorWasNotFound = uiState.authorWasNotFound,
                onClickSave = {
                    viewModel.sendEvent(BookCreatorEvents.CreateBookEvent)
                },
            )
        }

        AnimatedVisibility(uiState.showDatePicker) {
            val timePickerTitle =
                remember { if (uiState.datePickerType == DatePickerType.StartDate) Strings.start_date else Strings.end_date }
            viewModel.CommonDatePicker(
                state = dataPickerState,
                title = timePickerTitle,
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

            AnimatedVisibility(
                uiState.showFullScreenBookSelector,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }
                ) + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }
                ) + fadeOut(targetAlpha = 0.3f),
                modifier = Modifier.padding(it)
            ) {
                BookListSelector(uiState.similarBooks, platform = platform) { selectedBook ->
                    viewModel.sendEvent(BookCreatorEvents.OnBookSelected(selectedBook))
                }
            }
        }
    }
}