import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import book_editor.AuthorIsNotSelectedInfo
import book_editor.BookEditor
import containters.CenterBoxContainer
import date.CommonDatePicker
import date.DatePickerEvents
import di.Inject
import info.InfoBlock
import loader.LoadingStatusIndicator
import main_models.DatePickerType
import main_models.rest.LoadingStatus
import models.BookCreatorEvents
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.Platform
import platform.isDesktop
import platform.isMobile
import tags.CustomTag
import text_fields.TextFieldWithTitleAndSuggestion


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
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
        remember { mutableStateOf(TextFieldValue(text = uiState.defaultStatus.value.nameValue)) }
    val dataPickerState = rememberDatePickerState()
    val linkToAuthor = remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        uiState.clearAllAuthorInfo()
                        viewModel.sendEvent(BookCreatorEvents.GoBack)
                    },
                )
            },
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor
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
            Column(
                modifier = Modifier.padding(
                    start = if (platform.isDesktop()) 24.dp else 8.dp,
                    end = if (platform.isDesktop()) 24.dp else 8.dp,
                    top = 2.dp,
                    bottom = 16.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .sizeIn(maxHeight = 52.dp, minHeight = 52.dp)
                        .padding(top = 4.dp)
                ) {
                    CenterBoxContainer {
                        if (
                            platform.isDesktop() &&
                            uiState.selectedAuthor.value == null &&
                            uiState.similarSearchAuthors.isNotEmpty() && !uiState.needCreateNewAuthor.value
                        ) {
                            AuthorIsNotSelectedInfo()
                        } else if (
                            uiState.bookValues.value.isRequiredFieldsFilled() && uiState.needCreateNewAuthor.value ||
                            uiState.bookValues.value.isRequiredFieldsFilled() && uiState.selectedAuthor.value != null
                        ) {
                            CustomTag(
                                text = Strings.save,
                                color = ApplicationTheme.colors.mainAddButtonColor,
                                textStyle = ApplicationTheme.typography.footnoteBold,
                                textModifier = Modifier,
                                maxHeight = 50.dp,
                                onClick = { viewModel.sendEvent(BookCreatorEvents.CreateBookEvent) }
                            )
                        } else {
                            Text(
                                text = if (uiState.showParsingResult.value) uiState.bookValues.value.bookName.value.text else Strings.add_book,
                                modifier = Modifier.padding(top = 8.dp),
                                style = ApplicationTheme.typography.title2Bold,
                                color = ApplicationTheme.colors.mainTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Image(
                            painter = painterResource(Drawable.drawable_ic_close_128px),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                            modifier = Modifier.size(22.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    uiState.clearAllAuthorInfo()
                                    viewModel.sendEvent(BookCreatorEvents.GoBack)
                                }
                            )
                        )
                    }

                    if (uiState.isCreateBookManually.value && !uiState.showParsingResult.value) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(start = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = ApplicationTheme.colors.mainIconsColor,
                                modifier = Modifier.size(26.dp).clickable(
                                    interactionSource = MutableInteractionSource(),
                                    null
                                ) {
                                    viewModel.sendEvent(BookCreatorEvents.DisableCreateBookManuallyEvent)
                                }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollableState)
                ) {
                    AnimatedVisibility(
                        !uiState.isCreateBookManually.value,
                    ) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            TextFieldWithTitleAndSuggestion(
                                platform = platform,
                                title = Strings.link,
                                enabledInput = uiState.urlFieldIsWork.value,
                                modifier = Modifier,
                                hintText = Strings.hint_past_url_book,
                                setAsSelected = !uiState.showParsingResult.value,
                                showClearButton = uiState.showClearButtonOfUrlElement,
                                textFieldValue = uiState.bookValues.value.parsingUrl,
                                onTextChanged = { url ->
                                    viewModel.sendEvent(BookCreatorEvents.UrlTextChangedEvent(url))
                                },
                                onClearButtonListener = {
                                    uiState.showDialogClearAllData.value = true
                                }
                            )
                            AnimatedVisibility(visible = !uiState.showParsingResult.value) {
                                InfoBlock(
                                    Strings.tooltip_parsing_book,
                                    modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = uiState.showLoadingIndicator.value) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp).fillMaxWidth()
                        ) {
                            LoadingStatusIndicator(
                                loadingStatus = uiState.loadingStatus,
                                finishAnimationListener = {
                                    viewModel.sendEvent(BookCreatorEvents.OnFinishParsingUrl)
                                }
                            )
                            if (uiState.loadingStatus.value == LoadingStatus.ERROR) {
                                ShowError {
                                    viewModel.sendEvent(BookCreatorEvents.OnClearUrlAndCreateBookManuallyEvent)
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        !uiState.isCreateBookManually.value
                                && !uiState.showParsingResult.value
                                && !uiState.showLoadingIndicator.value,
                    ) {
                        CenterBoxContainer {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = Strings.or.uppercase(),
                                    style = ApplicationTheme.typography.bodyBold,
                                    color = ApplicationTheme.colors.mainTextColor,
                                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                                )
                                CreateBookButton(title = Strings.add_manually_button) {
                                    viewModel.sendEvent(BookCreatorEvents.OnCreateBookManuallyEvent)
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        uiState.isCreateBookManually.value || uiState.showParsingResult.value,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.padding(top = 12.dp))

                            viewModel.BookEditor(
                                platform = platform,
                                bookValues = uiState.bookValues.value,
                                similarSearchAuthors = uiState.similarSearchAuthors,
                                selectedAuthor = uiState.selectedAuthor,
                                createNewAuthor = uiState.needCreateNewAuthor,
                                linkToAuthor = linkToAuthor,
                                isKeyboardShown = isKeyboardShown,
                                statusBookTextFieldValue = statusBookTextFieldValue,
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(uiState.showDatePicker.value) {
                val timePickerTitle =
                    remember { if (uiState.datePickerType.value == DatePickerType.StartDate) Strings.start_date else Strings.end_date }
                viewModel.CommonDatePicker(
                    state = dataPickerState,
                    title = timePickerTitle,
                    onDismissRequest = {
                        viewModel.sendEvent(DatePickerEvents.OnHideDatePicker)
                    },
                )
            }

            AnimatedVisibility(
                visible = uiState.showDialogClearAllData.value,
                exit = fadeOut(animationSpec = tween(durationMillis = 1))
            ) {
                ClearDataAlertDialog(
                    onDismissRequest = {
                        uiState.showDialogClearAllData.value = false
                    }, onClick = {
                        viewModel.sendEvent(BookCreatorEvents.ClearUrlEvent)
                    }
                )
            }
        }
    }
}

@Composable
internal fun ShowError(
    createBookManuallyListener: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
    ) {
        Text(
            text = Strings.parsingError,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.errorColor,
            textAlign = TextAlign.Center
        )

        CreateBookButton(
            title = Strings.add_manually_button,
            onClick = createBookManuallyListener
        )
    }
}

@Composable
internal fun CreateBookButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = ApplicationTheme.colors.cardBackgroundLight)
    ) {
        Text(
            text = title,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            textAlign = TextAlign.Center
        )
    }
}