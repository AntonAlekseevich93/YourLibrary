import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import book_editor.BookValues
import di.Inject
import io.kamel.core.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import menu_bar.LeftMenuBar
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import platform.Platform
import platform.isDesktop
import text_fields.DELAY_FOR_LISTENER_PROCESSING
import tooltip_area.TooltipItem

@Composable
fun BookScreen(
    platform: Platform,
    bookItemId: String,
    fullScreenBookInfo: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    showLeftDrawer: MutableState<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    rightDrawerState: DrawerState,
    painterInCache: Resource<Painter>? = null,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    onClose: () -> Unit,
    createBookListener: () -> Unit,
    selectAnotherVaultListener: () -> Unit,
) {
    val viewModel = remember { Inject.instance<BookInfoViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val bookValues = remember { mutableStateOf(BookValues()) }
    uiState.bookItem.value?.let {
        bookValues.value.setBookItem(it)
    }

    LaunchedEffect(key1 = bookItemId) {
        viewModel.getBookItem(bookItemId)
    }

    val scope = rememberCoroutineScope()
    val leftMenuVisible = remember { mutableStateOf(false) }
    val background = if (fullScreenBookInfo.value || leftMenuVisible.value)
        ApplicationTheme.colors.mainBackgroundWindowDarkColor
    else {
        Color.Transparent
    }
    val isEditMode = remember { mutableStateOf(false) }

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

                LeftMenuBar(
                    searchListener = {
                        showSearch.value = true
                    },
                    tooltipCallback = tooltipCallback,
                    open = {

                    },
                    createBookListener = createBookListener,
                    selectAnotherVaultListener = selectAnotherVaultListener,
                )
            }
        }

        PlatformNavigationDrawer(
            platform = platform,
            leftDrawerContent = {
                AnimatedVisibility(visible = fullScreenBookInfo.value) {
                    Row {
                        PlatformLeftDrawerContent(
                            title = uiState.selectedPathInfo.value.libraryName,
                            platform = platform,
                            tooltipCallback = tooltipCallback,
                            closeSidebarListener = {
                                scope.launch {
                                    if (!showLeftDrawer.value) {
                                        showLeftDrawer.value = true
                                        leftDrawerState.open()
                                    } else {
                                        showLeftDrawer.value = false
                                        leftDrawerState.close()
                                    }
                                }
                            }
                        )
                        Divider(
                            modifier = Modifier.fillMaxHeight().width(1.dp),
                            thickness = 1.dp,
                            color = ApplicationTheme.colors.divider
                        )
                    }
                }
            },
            background = background,
            showLeftDrawer = showLeftDrawer,
        ) {
            Box(
                contentAlignment = if (platform.isDesktop()) Alignment.TopCenter else Alignment.TopStart,
            ) {
                if (uiState.bookItem.value != null) {
                    BookInfoScreen(
                        platform = platform,
                        painterInCache = painterInCache,
                        bookItem = uiState.bookItem.value!!,
                        bookValues = bookValues,
                        onClose = onClose,
                        fullScreenBookInfo = fullScreenBookInfo,
                        showLeftDrawer = showLeftDrawer,
                        showRightDrawer = showRightDrawer,
                        isEditMode = isEditMode,
                        similarAuthorList = uiState.similarAuthorList,
                        openLeftDrawerListener = {
                            scope.launch {
                                if (!showLeftDrawer.value) {
                                    showLeftDrawer.value = true
                                    leftDrawerState.open()
                                } else {
                                    showLeftDrawer.value = false
                                    leftDrawerState.close()
                                }
                            }
                        },
                        openRightDrawerListener = {
                            scope.launch {
                                if (!showRightDrawer.value) {
                                    showRightDrawer.value = true
                                    rightDrawerState.open()
                                } else {
                                    showRightDrawer.value = false
                                    rightDrawerState.close()
                                }
                            }
                        },
                        closeRightDrawerListener = {
                            scope.launch {
                                if (!showRightDrawer.value) {
                                    showRightDrawer.value = true
                                    rightDrawerState.open()
                                } else {
                                    showRightDrawer.value = false
                                    rightDrawerState.close()
                                }
                            }
                        },
                        tooltipCallback = tooltipCallback,
                        editBookCallback = {
                            if (isEditMode.value && uiState.bookItem.value != null) {
                                bookValues.value.updateBook(
                                    bookId = uiState.bookItem.value!!.id
                                )?.let {
                                    viewModel.updateBook(it)
                                }
                            }

                            isEditMode.value = !isEditMode.value
                        },
                        onAuthorTextChanged = {
                            bookValues.value.authorName.value =
                                bookValues.value.authorName.value.copy(
                                    it.text,
                                    selection = TextRange(it.text.length)
                                )
                            scope.launch {
                                delay(DELAY_FOR_LISTENER_PROCESSING)
                                viewModel.clearSearchAuthor()
                            }
                        },
                        onSuggestionAuthorClickListener = {
                            bookValues.value.authorName.value =
                                bookValues.value.authorName.value.copy(
                                    it,
                                    selection = TextRange(it.length)
                                )
                            scope.launch {
                                delay(DELAY_FOR_LISTENER_PROCESSING)
                                viewModel.clearSearchAuthor()
                            }
                        },
                        changeReadingStatusListener = {
                            viewModel.changeReadingStatus(it, bookItemId)
                        }
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
}