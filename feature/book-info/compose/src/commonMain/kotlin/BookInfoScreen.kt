import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import book_editor.BookEditor
import book_editor.BookValues
import date.CommonDatePicker
import io.kamel.core.Resource
import main_models.BookItemVo
import main_models.DatePickerType
import main_models.ReadingStatus
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.PlatformRightDrawerContent
import navigation_drawer.SelectedRightDrawerItem
import platform.Platform
import tooltip_area.TooltipItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoScreen(
    platform: Platform,
    bookItem: BookItemVo,
    bookValues: MutableState<BookValues>,
    similarAuthorList: MutableState<List<String>>,
    fullScreenBookInfo: MutableState<Boolean>,
    painterInCache: Resource<Painter>? = null,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    isEditMode: State<Boolean>,
    openLeftDrawerListener: () -> Unit,
    openRightDrawerListener: () -> Unit,
    closeRightDrawerListener: () -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    editBookCallback: () -> Unit,
    onAuthorTextChanged: (TextFieldValue) -> Unit,
    onSuggestionAuthorClickListener: (String) -> Unit,
    changeReadingStatusListener: (selectedStatus: ReadingStatus, oldStatusId: String) -> Unit,
    onClose: () -> Unit,
) {

    val targetVerticalPadding = if (fullScreenBookInfo.value) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (fullScreenBookInfo.value || platform == Platform.MOBILE) 0.dp else if (showRightDrawer.value) 100.dp else 220.dp
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
    val shapeInDp = if (fullScreenBookInfo.value) 0.dp else 8.dp
    val statusBookTextFieldValue =
        remember(key1 = bookItem.readingStatus) { mutableStateOf(TextFieldValue(text = bookItem.readingStatus.nameValue)) }
    val showDataPicker = remember { mutableStateOf(false) }
    val dataPickerState = rememberDatePickerState()
    var datePickerType by remember { mutableStateOf(DatePickerType.StartDate) }
    val scrollableState = rememberScrollState()
    val hideSaveButton = remember { mutableStateOf(false) }
    hideSaveButton.value = bookValues.value.bookName.value.text.isEmpty() ||
            bookValues.value.authorName.value.text.isEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClose.invoke()
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(shapeInDp),
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
            PlatformNavigationDrawer(
                platform = platform,
                rightDrawerContent = {
                    AnimatedVisibility(visible = showRightDrawer.value) {
                        Row {
                            Divider(
                                modifier = Modifier.fillMaxHeight().width(1.dp),
                                thickness = 1.dp,
                                color = ApplicationTheme.colors.divider
                            )
                            PlatformRightDrawerContent(
                                platform = platform,
                                isFullscreen = fullScreenBookInfo,
                                isCanClose = true,
                                selectedItem = SelectedRightDrawerItem.STRUCTURE,
                                closeSidebarListener = closeRightDrawerListener,
                                expandOrCollapseListener = {
                                    fullScreenBookInfo.value = !fullScreenBookInfo.value
                                },
                                closeWindow = onClose,
                                tooltipCallback = tooltipCallback
                            )
                        }
                    }
                },
                background = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
                showRightDrawer = showRightDrawer
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FullBookBar(
                        platform = platform,
                        showLeftDrawer = showLeftDrawer,
                        showRightDrawer = showRightDrawer,
                        hideSaveButton = hideSaveButton,
                        onClose = onClose,
                        onFullscreen = { fullScreenBookInfo.value = !fullScreenBookInfo.value },
                        isFullscreen = fullScreenBookInfo,
                        openLeftDrawerListener = openLeftDrawerListener,
                        openRightDrawerListener = openRightDrawerListener,
                        isEditMode = isEditMode,
                        editBookCallback = editBookCallback,
                        tooltipCallback = tooltipCallback
                    )

                    Divider(
                        modifier = Modifier.fillMaxWidth().height(1.dp),
                        color = ApplicationTheme.colors.divider
                    )

                    Column(modifier = Modifier.verticalScroll(scrollableState)) {
                        AnimatedVisibility(isEditMode.value) {
                            BookEditor(
                                platform = platform,
                                bookValues = bookValues.value,
                                similarAuthorList = similarAuthorList,
                                statusBookTextFieldValue = statusBookTextFieldValue,
                                buttonTitle = Strings.save,
                                modifier = Modifier.padding(top = 24.dp),
                                showDataPickerListener = {
                                    datePickerType = it
                                    showDataPicker.value = true
                                },
                                onAuthorTextChanged = onAuthorTextChanged,
                                onSuggestionAuthorClickListener = onSuggestionAuthorClickListener,
                                saveBook = editBookCallback
                            )
                        }
                    }

                    AnimatedVisibility(!isEditMode.value) {
                        BookInfoContent(
                            platform = platform,
                            bookItem = bookItem,
                            painterInCache = painterInCache,
                            changeReadingStatusListener = changeReadingStatusListener,
                        )
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
                                bookValues.value.startDateInMillis.value = millis
                                bookValues.value.startDateInString.value = text
                            }

                            DatePickerType.EndDate -> {
                                bookValues.value.endDateInMillis.value = millis
                                bookValues.value.endDateInString.value = text
                            }
                        }
                    }
                )
            }
        }
    }
}