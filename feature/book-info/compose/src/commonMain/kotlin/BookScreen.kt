import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import di.Inject
import io.kamel.core.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.Platform
import platform.isDesktop

@Composable
fun BookScreen(
    platform: Platform,
    bookItemId: String,
    fullScreenBookInfo: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    showLeftDrawer: MutableState<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    isKeyboardShown: State<Boolean>,
    painterInCache: Resource<Painter>? = null,
) {
    val viewModel = remember { Inject.instance<BookInfoViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val bookValues = uiState.bookValues
    uiState.bookItem.value?.let {
        //todo
//        bookValues.value.setBookItem(it)
    }

    LaunchedEffect(key1 = bookItemId) {
        //todo
//        viewModel.getBookItem(bookItemId)
    }

    val scope = rememberCoroutineScope()
    val leftMenuVisible = remember { mutableStateOf(false) }
    val background = if (fullScreenBookInfo.value || leftMenuVisible.value)
        ApplicationTheme.colors.mainBackgroundWindowDarkColor
    else {
        Color.Transparent
    }

    /** this is necessary to get rid of the white blinking effect due
     * to the background transparent when collapsing and expanding */
    LaunchedEffect(fullScreenBookInfo.value) {
        if (!fullScreenBookInfo.value && leftMenuVisible.value) {
            scope.launch {
                delay(300)
                leftMenuVisible.value = false
            }
        }
    }

    Row(
        modifier = Modifier.background(background)
    ) {
        AnimatedVisibility(
            visible = fullScreenBookInfo.value,
        ) {
            if (platform.isDesktop()) {
                if (fullScreenBookInfo.value) {
                    /** this is necessary to get rid of the white blinking effect due
                     * to the background transparent when collapsing and expanding */
                    scope.launch {
                        delay(200)
                        leftMenuVisible.value = true
                    }
                }
            }
        }

        Box(
            contentAlignment = if (platform.isDesktop()) Alignment.TopCenter else Alignment.TopStart,
        ) {
            if (uiState.bookItem.value != null) {
                viewModel.BookScreenContent(
                    platform = platform,
                    painterInCache = painterInCache,
                    bookItem = uiState.bookItem.value!!,
                    bookValues = bookValues,
                    fullScreenBookInfo = fullScreenBookInfo,
                    showLeftDrawer = showLeftDrawer,
                    showRightDrawer = showRightDrawer,
                    isEditMode = uiState.isEditMode,
                    isKeyboardShown = isKeyboardShown,
                    similarAuthorList = uiState.similarSearchAuthors,
                    selectedAuthor = uiState.selectedAuthor,
                    needCreateNewAuthor = uiState.needCreateNewAuthor,
                    datePickerType = uiState.datePickerType,
                    showDatePicker = uiState.showDatePicker
                )
            }
            CustomDockedSearchBar(
                showSearch = showSearch,
                closeSearch = {
                    showSearch.value = false
                },
            )
        }
    }
}