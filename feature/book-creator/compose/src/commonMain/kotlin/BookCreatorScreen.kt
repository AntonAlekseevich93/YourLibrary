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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import book_editor.AuthorIsNotSelectedInfo
import book_editor.BookEditor
import book_editor.BookValues
import containters.CenterBoxContainer
import date.CommonDatePicker
import di.Inject
import info.InfoBlock
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import loader.LoadingStatusIndicator
import main_models.BookItemVo
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.rest.LoadingStatus
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.Platform
import platform.isDesktop
import platform.isMobile
import tags.CustomTag
import text_fields.DELAY_FOR_LISTENER_PROCESSING
import text_fields.TextFieldWithTitleAndSuggestion
import tooltip_area.TooltipItem


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun BookCreatorScreen(
    platform: Platform,
    fullScreenBookCreator: MutableState<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    isKeyboardShown: State<Boolean>,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    closeBookCreatorListener: () -> Unit,
) {
    val viewModel = remember { Inject.instance<BookCreatorViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollableState = rememberScrollState()
    val bookValues by remember { mutableStateOf(BookValues()) }

    val targetVerticalPadding =
        if (fullScreenBookCreator.value || platform.isMobile()) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (fullScreenBookCreator.value || platform == Platform.MOBILE) 0.dp else if (showRightDrawer.value) 100.dp else 220.dp

    val isCreateBookManually = remember { mutableStateOf(false) }
    val statusBookTextFieldValue =
        remember { mutableStateOf(TextFieldValue(text = uiState.defaultStatus.value.nameValue)) }
    val showDataPicker = remember { mutableStateOf(false) }
    val dataPickerState = rememberDatePickerState()
    var datePickerType by remember { mutableStateOf(DatePickerType.StartDate) }
    val showParsingResult = remember { mutableStateOf(false) }
    val urlFieldIsWork = remember { mutableStateOf(true) }
    val showDialogClearAllData = remember { mutableStateOf(false) }
    val showClearButtonOfUrlElement = remember { mutableStateOf(false) }
    val createNewAuthor = remember { mutableStateOf(false) }
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

    LaunchedEffect(key1 = uiState.needUpdateBookInfo.value) {
        if (uiState.needUpdateBookInfo.value) {
            urlFieldIsWork.value = false
            uiState.bookItem.value?.let { book ->
                updateBookValues(bookValues = bookValues, book = book)
            }
            uiState.needUpdateBookInfo.value = false
        }
    }

    uiState.authorWasSelectedProgrammatically.value = {
        uiState.selectedAuthor.value?.let { author ->
            val textPostfix = if (author.relatedAuthors.isNotEmpty()) {
                "(${author.relatedAuthors.joinToString { it.name }})"
            } else ""
            bookValues.setSelectedAuthorName(
                author.name,
                relatedAuthorsNames = textPostfix
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        uiState.clearAllAuthorInfo()
                        closeBookCreatorListener.invoke()
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
                            uiState.similarSearchAuthors.isNotEmpty() && !createNewAuthor.value
                        ) {
                            AuthorIsNotSelectedInfo()
                        } else if (
                            bookValues.isRequiredFieldsFilled() && createNewAuthor.value ||
                            bookValues.isRequiredFieldsFilled() && uiState.selectedAuthor.value != null
                        ) {
                            CustomTag(
                                text = Strings.save,
                                color = ApplicationTheme.colors.mainAddButtonColor,
                                textStyle = ApplicationTheme.typography.footnoteBold,
                                textModifier = Modifier,
                                maxHeight = 50.dp,
                                onClick = {
                                    viewModel.createBook(
                                        bookItemVoOrNull = bookValues.createBookItemWithoutAuthorIdOrNull(
                                            timestampOfCreating = viewModel.getCurrentTimeInMillis(),
                                            timestampOfUpdating = viewModel.getCurrentTimeInMillis(),
                                        ),
                                        needCreateAuthor = createNewAuthor.value
                                    )
                                    uiState.clearAllAuthorInfo()
                                    closeBookCreatorListener.invoke()
                                }
                            )
                        } else {
                            Text(
                                text = if (showParsingResult.value) bookValues.bookName.value.text else Strings.add_book,
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
                                    closeBookCreatorListener.invoke()
                                }
                            )
                        )
                    }

                    if (isCreateBookManually.value && !showParsingResult.value) {
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
                                    isCreateBookManually.value = false
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
                        !isCreateBookManually.value,
                    ) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            TextFieldWithTitleAndSuggestion(
                                platform = platform,
                                title = Strings.link,
                                enabledInput = urlFieldIsWork.value,
                                modifier = Modifier,
                                hintText = Strings.hint_past_url_book,
                                setAsSelected = !showParsingResult.value,
                                showClearButton = showClearButtonOfUrlElement,
                                textFieldValue = bookValues.parsingUrl,
                                onTextChanged = { url ->
                                    bookValues.parsingUrl.value = url
                                    if (url.text.isNotEmpty() && url.text.length > 5) {
                                        viewModel.startParseBook(url = url.text)
                                    } else {
                                        viewModel.stopParsingBook()
                                    }
                                },
                                onClearButtonListener = {
                                    showDialogClearAllData.value = true
                                }
                            )
                            AnimatedVisibility(visible = !showParsingResult.value) {
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
                                    if (uiState.loadingStatus.value == LoadingStatus.SUCCESS) {
                                        showClearButtonOfUrlElement.value = true
                                        viewModel.hideLoadingStatusIndicator()
                                        showParsingResult.value = true
                                    } else {

                                    }
                                }
                            )
                            if (uiState.loadingStatus.value == LoadingStatus.ERROR) {
                                ShowError {
                                    bookValues.parsingUrl.value = TextFieldValue()
                                    isCreateBookManually.value = true
                                    viewModel.hideLoadingStatusIndicator()
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        !isCreateBookManually.value
                                && !showParsingResult.value
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
                                    bookValues.parsingUrl.value = TextFieldValue()
                                    isCreateBookManually.value = true
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        isCreateBookManually.value || showParsingResult.value,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.padding(top = 12.dp))

                            BookEditor(
                                platform = platform,
                                bookValues = bookValues,
                                similarSearchAuthors = uiState.similarSearchAuthors,
                                selectedAuthor = uiState.selectedAuthor,
                                createNewAuthor = createNewAuthor,
                                linkToAuthor = linkToAuthor,
                                isKeyboardShown = isKeyboardShown,
                                onAuthorTextChanged = { newValue, textWasChanged ->
                                    if (uiState.selectedAuthor.value != null && bookValues.isSelectedAuthorNameWasChanged()) {
                                        uiState.clearSelectedAuthor()
                                    }

                                    if (newValue.text.isEmpty()) {
                                        viewModel.clearSearchAuthor()
                                    } else if (textWasChanged) {
                                        viewModel.searchAuthor(newValue.text)
                                    }
                                },
                                statusBookTextFieldValue = statusBookTextFieldValue,
                                showDataPickerListener = {
                                    datePickerType = it
                                    showDataPicker.value = true
                                },
                                onSuggestionAuthorClickListener = { author ->
                                    viewModel.setSelectedAuthor(author)
                                    scope.launch {
                                        delay(DELAY_FOR_LISTENER_PROCESSING)
                                        viewModel.clearSearchAuthor()
                                    }
                                },
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(showDataPicker.value) {
                val timePickerTitle =
                    remember { if (datePickerType == DatePickerType.StartDate) Strings.start_date else Strings.end_date }
                CommonDatePicker(
                    state = dataPickerState,
                    title = timePickerTitle,
                    onDismissRequest = {
                        showDataPicker.value = false
                    },
                    onSelectedListener = { millis, text ->
                        when (datePickerType) {
                            DatePickerType.StartDate -> {
                                bookValues.startDateInMillis.value = millis
                                bookValues.startDateInString.value = text
                            }

                            DatePickerType.EndDate -> {
                                bookValues.endDateInMillis.value = millis
                                bookValues.endDateInString.value = text
                            }
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = showDialogClearAllData.value,
                exit = fadeOut(animationSpec = tween(durationMillis = 1))
            ) {
                ClearDataAlertDialog(
                    onDismissRequest = {
                        showDialogClearAllData.value = false
                    }, onClick = {
                        urlFieldIsWork.value = true
                        showClearButtonOfUrlElement.value = false
                        showParsingResult.value = false
                        bookValues.clearAll()
                        viewModel.clearAllBookData()
                        showDialogClearAllData.value = false
                    }
                )
            }
        }
    }
}

private fun updateBookValues(
    bookValues: BookValues,
    book: BookItemVo
) {
    bookValues.apply {
        authorName.value = authorName.value.copy(
            text = book.authorName,
            selection = TextRange(book.authorName.length)
        )
        bookName.value = bookName.value.copy(
            text = book.bookName,
            selection = TextRange(book.bookName.length)
        )
        numberOfPages.value = numberOfPages.value.copy(
            text = book.numbersOfPages.toString(),
            selection = TextRange(book.numbersOfPages.toString().length)
        )
        description.value = description.value.copy(
            text = book.description,
            selection = TextRange(book.description.length)
        )
        selectedStatus.value = ReadingStatus.PLANNED

        coverUrl.value = coverUrl.value.copy(
            text = book.coverUrlFromParsing,
            selection = TextRange(book.coverUrlFromParsing.length)
        )
        isbn.value = isbn.value.copy(
            text = book.isbn,
            selection = TextRange(book.isbn.length)
        )
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