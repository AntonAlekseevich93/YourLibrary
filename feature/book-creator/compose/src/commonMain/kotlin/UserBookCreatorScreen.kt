import alert_dialog.CommonAlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import animations.SuccessAnimation
import animations.ToggleServiceDevelopmentAnimation
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import containters.CenterColumnContainer
import date.CommonDatePicker
import date.DatePickerEvents
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import genre.GenreSelector
import main_models.DatePickerType
import models.BookCreatorEvents
import navigation.screen_components.UserBookCreatorScreenComponent
import reading_status.ReadingStatusSelectorDialog
import user_book_creator_screen.BookCreatorAuthorElement
import user_book_creator_screen.BookCreatorBookNameElement
import user_book_creator_screen.BookCreatorDateElement
import user_book_creator_screen.BookCreatorDescriptionElement
import user_book_creator_screen.BookCreatorGenreElement
import user_book_creator_screen.BookCreatorImageUrlElement
import user_book_creator_screen.BookCreatorIsbnElement
import user_book_creator_screen.BookCreatorLangElement
import user_book_creator_screen.BookCreatorPagesElement
import user_book_creator_screen.BookCreatorReadingStatusElement
import user_book_creator_screen.BookCreatorSaveButton
import user_book_creator_screen.BookCreatorServiceDevelopment
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserBookCreatorScreen(
    hazeState: HazeState,
    navigationComponent: UserBookCreatorScreenComponent,
) {
    val viewModel = remember { Inject.instance<BookCreatorViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val dataPickerState = rememberDatePickerState()
    val scrollState = rememberScrollState()
    var selectionGenreState by remember { mutableStateOf(false) }
    val showSuccessAnimation = remember { mutableStateOf(false) }
    val showServiceDevelopmentAnimation = remember { mutableStateOf(false) }
    var isServiceDevelopment by remember { mutableStateOf(false) }

    var hazeModifier: Modifier = Modifier
    if (uiState.isHazeBlurEnabled.value) {
        hazeModifier = Modifier.haze(
            state = hazeState,
            style = HazeStyle(
                tint = Color.Black.copy(alpha = .04f),
                blurRadius = 30.dp,
            )
        )
    }

    Scaffold(
        topBar = {
            BookCreatorAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = "Создание книги",
                showBackButton = true,
                onBack = {
                    navigationComponent.onBack()
                },
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
        snackbarHost = {
            SnackbarHost(hostState = uiState.snackbarHostState)
        },
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp) //fixes haze blur bug
                .verticalScroll(scrollState)
                .background(Color.Transparent)
        ) {
            Column(
                modifier = hazeModifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding().plus(16.dp),
                        bottom = it.calculateBottomPadding().plus(16.dp)
                    )
            ) {
                BookCreatorServiceDevelopment(
                    serviceDevelopment = isServiceDevelopment,
                    serviceDevelopmentCallback = {
                        isServiceDevelopment = !isServiceDevelopment
                        if (isServiceDevelopment) {
                            showServiceDevelopmentAnimation.value = true
                        }
                    }
                )
                val imageModifier = Modifier.sizeIn(
                    minHeight = 160.dp,
                    minWidth = 95.dp,
                    maxHeight = 160.dp,
                    maxWidth = 95.dp
                )
                CenterColumnContainer {
                    Card(
                        modifier = imageModifier.padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
                        shape = RoundedCornerShape(6.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        AsyncImage(
                            modifier = imageModifier,
                            request = ComposableImageRequest(uiState.userBookCreatorUiState.imageUrlTextFieldValue.value.text) {
                                scale(Scale.FILL)
                                placeholder(Res.drawable.ic_default_book_cover_7)
                                error(Res.drawable.ic_default_book_cover_7)
                            },
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null,
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Обязательные поля отмечены ",
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                    Text(
                        text = "*",
                        style = ApplicationTheme.typography.title1Regular,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                }

                BookCreatorBookNameElement(
                    isEnabled = uiState.bookValues.bookName.value.text.isEmpty(),
                    textState = uiState.userBookCreatorUiState.bookNameTextState,
                )
                BookCreatorAuthorElement(
                    isEnabled = uiState.bookValues.authorName.value.text.isEmpty(),
                    textState = uiState.userBookCreatorUiState.authorNameTextState,
                )
                BookCreatorImageUrlElement(
                    imageUrlTextFieldValue = uiState.userBookCreatorUiState.imageUrlTextFieldValue,
                    isServiceDevelopment = isServiceDevelopment
                )
                BookCreatorPagesElement(
                    textState = uiState.userBookCreatorUiState.pagesTextState,
                    isServiceDevelopment = isServiceDevelopment
                )
                BookCreatorGenreElement(
                    selectedGenre = uiState.userBookCreatorUiState.selectedGenre,
                    onClick = { selectionGenreState = true }
                )
                BookCreatorReadingStatusElement(
                    readingStatus = uiState.userBookCreatorUiState.readingStatus,
                    changeStatusListener = {
                        uiState.userBookCreatorUiState.readingStatus.value = it
                    }
                )
                BookCreatorDateElement(
                    onStartDateClick = {},
                    onEndDateClick = {}
                )

                BookCreatorIsbnElement(
                    textState = uiState.userBookCreatorUiState.isbnTextState,
                    isServiceDevelopment = isServiceDevelopment
                )

                BookCreatorDescriptionElement(
                    textFieldValue = uiState.userBookCreatorUiState.descriptionTextState
                )

                BookCreatorLangElement(
                    selectedLang = uiState.userBookCreatorUiState.selectedLang,
                    selectedLangListener = {
                        uiState.userBookCreatorUiState.selectedLang.value = it
                    }
                )
                BookCreatorSaveButton()
            }
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
                    if (!showSuccessAnimation.value) {
                        showSuccessAnimation.value = true
                    }
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
                    uiState.userBookCreatorUiState.selectedGenre.value = genre
                    selectionGenreState = false
                }
            }
        }

        SuccessAnimation(
            show = showSuccessAnimation,
            finishAnimation = {
                showSuccessAnimation.value = false
            }
        )

        ToggleServiceDevelopmentAnimation(
            show = showServiceDevelopmentAnimation,
            finishAnimation = {
                showServiceDevelopmentAnimation.value = false
            }
        )
    }
}