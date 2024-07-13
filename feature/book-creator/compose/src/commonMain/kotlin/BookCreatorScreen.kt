import alert_dialog.CommonAlertDialog
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import book_editor.BookEditor
import book_list_selector.BookListSelector
import containters.CenterBoxContainer
import date.CommonDatePicker
import date.DatePickerEvents
import di.Inject
import genre.GenreSelector
import main_models.DatePickerType
import models.BookCreatorEvents
import org.jetbrains.compose.resources.painterResource
import platform.Platform
import platform.isDesktop
import platform.isMobile
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_arrow_back
import yourlibrary.common.resources.generated.resources.ic_main_close
import yourlibrary.common.resources.generated.resources.ic_main_search


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCreatorScreen(
    platform: Platform,
    fullScreenBookCreator: MutableState<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    isKeyboardShown: State<Boolean>,
) {
    val viewModel = remember { Inject.instance<BookCreatorViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollableState = rememberScrollState()

    val targetVerticalPadding =
        if (fullScreenBookCreator.value || platform.isMobile()) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (fullScreenBookCreator.value || platform.isMobile()) 0.dp else if (showRightDrawer.value) 100.dp else 220.dp
    val statusBookTextFieldValue =
        remember { mutableStateOf(TextFieldValue(text = uiState.defaultStatus.nameValue)) }
    val dataPickerState = rememberDatePickerState()
    val animatedVerticalPadding by animateDpAsState(
        targetValue = targetVerticalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )
    val animatedHorizontalPadding by animateDpAsState(
        targetValue = targetHorizontalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )

    var selectionGenreState by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        viewModel.sendEvent(BookCreatorEvents.GoBack)
                    },
                )
            },
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.screenColor.background
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = animatedVerticalPadding,
                    horizontal = animatedHorizontalPadding
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            /** это нужно чтобы перехватывать onPress
                             * на корневом Box который закрывает поиск*/
                        },
                    )
                },
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = ApplicationTheme.colors.screenColor.appBarColor),
                        title = {
                            CenterBoxContainer {
                                Text(
                                    text = if (uiState.isCreateBookManually || uiState.shortBookItem != null) Strings.add_book else "Поиск",
                                    style = ApplicationTheme.typography.title2Bold,
                                    color = ApplicationTheme.colors.screenColor.textColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        },
                        navigationIcon = {
                            if (uiState.isCreateBookManually || uiState.shortBookItem != null) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_main_search),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.screenColor.iconColor),
                                    modifier = Modifier.padding(start = 24.dp).size(22.dp)
                                        .clickable(
                                            interactionSource = MutableInteractionSource(),
                                            indication = null,
                                            onClick = {
                                                viewModel.sendEvent(
                                                    BookCreatorEvents.OnShowCommonAlertDialog(
                                                        CommonAlertDialogConfig(
                                                            title = Strings.alert_dialog_go_to_search_and_clear_data_title,
                                                            description = Strings.alert_dialog_go_to_search_and_clear_data_description,
                                                            acceptButtonTitle = Strings.alert_dialog_go_to_search_and_clear_data_button_ok,
                                                            dismissButtonTitle = Strings.alert_dialog_go_to_search_and_clear_data_button_dismiss,
                                                            acceptEvent = BookCreatorEvents.ClearAllBookInfo,
                                                            dismissEvent = BookCreatorEvents.DismissCommonAlertDialog
                                                        )
                                                    )
                                                )
                                            }
                                        ),
                                )
                            } else if (uiState.showFullScreenBookSelector) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_arrow_back),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.screenColor.iconColor),
                                    modifier = Modifier.padding(start = 24.dp).size(22.dp)
                                        .clickable(
                                            interactionSource = MutableInteractionSource(),
                                            indication = null,
                                            onClick = {
                                                viewModel.sendEvent(BookCreatorEvents.HideFullScreenBookSelector)
                                            }
                                        ),
                                )
                            }
                        },
                        actions = {
                            Image(
                                painter = painterResource(Res.drawable.ic_main_close),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(ApplicationTheme.colors.screenColor.iconColor),
                                modifier = Modifier.padding(end = 24.dp).size(22.dp).clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        viewModel.sendEvent(BookCreatorEvents.GoBack)
                                    }
                                ),
                            )
                        }
                    )
                },
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                snackbarHost = {
                    SnackbarHost(hostState = uiState.snackbarHostState)
                }
            ) {
                Column(
                    modifier = Modifier.padding(it).padding(
                        start = if (platform.isDesktop()) 24.dp else 8.dp,
                        end = if (platform.isDesktop()) 24.dp else 8.dp,
                        top = 2.dp,
                        bottom = 16.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollableState)
                    ) {
                        Column {
                            Spacer(modifier = Modifier.padding(top = 12.dp))

                            viewModel.BookEditor(
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
                                }
                            )
                        }
                    }
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
}