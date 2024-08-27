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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import date.CommonDatePicker
import date.DateChangeSelectorDialog
import date.DatePickerEvents
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.ViewModelStackStore
import kotlinx.coroutines.launch
import main_models.DatePickerType
import main_models.books.BookShortVo
import models.BookScreenEvents
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.end_date
import yourlibrary.common.resources.generated.resources.start_date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoScreen(
    bookItemId: Long?,
    bookShortVo: BookShortVo?,
    showBackButton: State<Boolean>,
    previousViewModel: BookInfoViewModel?,
) {
    val viewModel = remember {
        previousViewModel ?: ViewModelStackStore.createViewModel<BookInfoViewModel>()
    }
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val appBarHazeState = remember { HazeState() }
    val scrollState = rememberScrollState()
    val isTransparentAppbar = remember { mutableStateOf(true) }
    var reviewButtonPosition by remember { mutableStateOf(0) }
    var showDateSelectorDialog by remember { mutableStateOf(false) }

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
        if (previousViewModel == null &&
            viewModel.uiState.value.shortBookItem.value == null && viewModel.uiState.value.bookItem.value == null
        ) {
            bookItemId?.let {
                viewModel.getBookByLocalId(bookItemId)
            }
            bookShortVo?.let {
                viewModel.setShortBook(it)
            }
        }
    }

    Scaffold(
        topBar = {
            BookCreatorAppBar(
                transparentAppbar = isTransparentAppbar,
                title = "",
                showBackButton = showBackButton,
                onClose = {
                    viewModel.sendEvent(BookScreenEvents.CloseBookInfoScreen)
                },
                onBack = {
                    viewModel.sendEvent(BookScreenEvents.OnBack)
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
                    reviewButtonPosition = {
                        reviewButtonPosition = it
                    },
                    scrollToReviewButtonListener = {
                        scope.launch {
                            scrollState.animateScrollTo(reviewButtonPosition)
                        }
                    },
                    showDateSelectorDialog = {
                        showDateSelectorDialog = true
                    }
                )

                if (showDateSelectorDialog) {
                    DateChangeSelectorDialog(
                        startDateSelected = uiState.bookItem.value?.startDateInMillis?.takeIf { it > 0 } != null,
                        endDateSelected = uiState.bookItem.value?.endDateInMillis?.takeIf { it > 0 } != null,
                        selectDateTypeListener = {
                            viewModel.sendEvent(BookScreenEvents.ShowDateSelector(it))
                            showDateSelectorDialog = false
                        },
                        onDeleteDateListener = {
                            viewModel.sendEvent(DatePickerEvents.OnDeleteDate(it))
                        },
                        dismiss = {
                            showDateSelectorDialog = false
                        }
                    )
                }

                if (uiState.showDatePicker.value) {
                    val timePickerTitle = remember {
                        if (uiState.datePickerType.value == DatePickerType.StartDate)
                            Res.string.start_date else Res.string.end_date
                    }
                    viewModel.CommonDatePicker(
                        state = rememberDatePickerState(),
                        title = stringResource(timePickerTitle),
                        datePickerType = uiState.datePickerType.value,
                        minimumDate = if (uiState.datePickerType.value == DatePickerType.EndDate) {
                            uiState.bookItem.value?.startDateInMillis
                        } else null,
                        onDismissRequest = {
                            viewModel.sendEvent(DatePickerEvents.OnHideDatePicker)
                        },
                    )
                }
            }
        }
    }
}