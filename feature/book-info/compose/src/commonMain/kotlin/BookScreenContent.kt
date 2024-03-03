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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import book_editor.BookEditor
import date.CommonDatePicker
import date.DatePickerEvents
import io.kamel.core.Resource
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.BookValues
import main_models.DatePickerType
import models.BookScreenEvents
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.PlatformRightDrawerContent
import navigation_drawer.SelectedRightDrawerItem
import platform.Platform
import platform.isMobile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseEventScope<BaseEvent>.BookScreenContent(
    platform: Platform,
    bookItem: BookItemVo,
    bookValues: MutableState<BookValues>,
    similarAuthorList: SnapshotStateList<AuthorVo>,
    selectedAuthor: State<AuthorVo?>,
    fullScreenBookInfo: MutableState<Boolean>,
    painterInCache: Resource<Painter>? = null,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    isEditMode: State<Boolean>,
    needCreateNewAuthor: MutableState<Boolean>,
    isKeyboardShown: State<Boolean>,
    datePickerType: State<DatePickerType>,
    showDatePicker: State<Boolean>,
) {

    val targetVerticalPadding =
        if (fullScreenBookInfo.value || showLeftDrawer.value) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (fullScreenBookInfo.value || platform.isMobile() || showLeftDrawer.value) 0.dp else if (showRightDrawer.value) 100.dp else 220.dp
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
    val shapeInDp = if (fullScreenBookInfo.value || showLeftDrawer.value) 0.dp else 8.dp
    val statusBookTextFieldValue =
        remember(key1 = bookItem.readingStatus) { mutableStateOf(TextFieldValue(text = bookItem.readingStatus.nameValue)) }
    val dataPickerState = rememberDatePickerState()
    val scrollableState = rememberScrollState()
    val hideSaveButton = remember { mutableStateOf(false) }
    val linkToAuthor = remember { mutableStateOf(false) }
    hideSaveButton.value = bookValues.value.bookName.value.text.isEmpty() ||
            bookValues.value.authorName.value.text.isEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        this@BookScreenContent.sendEvent(BookScreenEvents.BookScreenCloseEvent)
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
                                expandOrCollapseListener = {
                                    fullScreenBookInfo.value = !fullScreenBookInfo.value
                                },
                                closeWindow = {
                                    this@BookScreenContent.sendEvent(BookScreenEvents.BookScreenCloseEvent)
                                },
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
                        onFullscreen = { fullScreenBookInfo.value = !fullScreenBookInfo.value },
                        isFullscreen = fullScreenBookInfo,
                        isEditMode = isEditMode,
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
                                similarSearchAuthors = similarAuthorList,
                                selectedAuthor = selectedAuthor,
                                statusBookTextFieldValue = statusBookTextFieldValue,
                                canShowError = true,
                                modifier = Modifier.padding(
                                    top = 24.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                                createNewAuthor = needCreateNewAuthor,
                                linkToAuthor = linkToAuthor,
                                isKeyboardShown = isKeyboardShown,
                            )
                        }
                    }

                    AnimatedVisibility(!isEditMode.value) {
                        BookContent(
                            platform = platform,
                            bookItem = bookItem,
                            painterInCache = painterInCache,
                        )
                    }
                }
            }

            AnimatedVisibility(showDatePicker.value) {
                val timePickerTitle =
                    remember { if (datePickerType.value == DatePickerType.StartDate) Strings.start_date else Strings.end_date }
                CommonDatePicker(
                    state = dataPickerState,
                    title = timePickerTitle,
                    onDismissRequest = {
                        this@BookScreenContent.sendEvent(DatePickerEvents.OnHideDatePicker)
                    },
                )
            }
        }
    }
}