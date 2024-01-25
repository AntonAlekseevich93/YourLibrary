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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import book_editor.BookValues
import di.Inject
import io.kamel.core.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main_models.BookItemVo
import menu_bar.LeftMenuBar
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import platform.Platform
import platform.isDesktop
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
    isKeyboardShown: State<Boolean>,
    painterInCache: Resource<Painter>? = null,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    onClose: () -> Unit,
    createBookListener: () -> Unit,
    changeReadingStatusListener: (oldStatusId: String, bookId: String) -> Unit,
    bookItemWasChangedListener: (oldItem: BookItemVo, newItem: BookItemVo) -> Unit,
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

    uiState.authorWasSelectedProgrammatically.value = {
        uiState.selectedAuthor.value?.let { author ->
            val textPostfix = if (author.relatedAuthors.isNotEmpty()) {
                "(${author.relatedAuthors.joinToString { it.name }})"
            } else ""
            bookValues.value.setSelectedAuthorName(
                author.name,
                relatedAuthorsNames = textPostfix
            )
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
                            },
                            content = {

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
                    BookScreenContent(
                        platform = platform,
                        painterInCache = painterInCache,
                        bookItem = uiState.bookItem.value!!,
                        bookValues = bookValues,
                        onClose = onClose,
                        fullScreenBookInfo = fullScreenBookInfo,
                        showLeftDrawer = showLeftDrawer,
                        showRightDrawer = showRightDrawer,
                        isEditMode = isEditMode,
                        isKeyboardShown = isKeyboardShown,
                        similarAuthorList = uiState.similarSearchAuthors,
                        selectedAuthor = uiState.selectedAuthor,
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
                        editBookModeCallback = { needCreateNewAuthor ->
                            if (isEditMode.value && uiState.bookItem.value != null) {
                                bookValues.value.updateBookWithEmptyAuthorId(
                                    bookId = uiState.bookItem.value!!.id,
                                    timestampOfCreating = uiState.bookItem.value!!.timestampOfCreating,
                                    timestampOfUpdating = viewModel.getCurrentTimeInMillis(),
                                )?.let {
                                    bookItemWasChangedListener.invoke(uiState.bookItem.value!!, it)
                                    viewModel.updateBook(
                                        bookItem = it,
                                        needCreateNewAuthor = needCreateNewAuthor
                                    )
                                }
                            }

                            isEditMode.value = !isEditMode.value
                        },
                        onAuthorTextChanged = { newValue, textWasChanged ->
                            if (uiState.selectedAuthor.value != null && bookValues.value.isSelectedAuthorNameWasChanged()) {
                                viewModel.clearSelectedAuthor()
                            }
                            if (newValue.text.isEmpty()) {
                                viewModel.clearSearchAuthor()
                            } else if (textWasChanged) {
                                viewModel.searchAuthor(newValue.text)
                            }
                        },
                        onSuggestionAuthorClickListener = { author ->
                            viewModel.setSelectedAuthor(author)
                            bookValues.value.authorName.value =
                                bookValues.value.authorName.value.copy(
                                    author.name,
                                    selection = TextRange(author.name.length)
                                )
                            viewModel.clearSearchAuthor()
                        },
                        changeReadingStatusListener = { selectedStatus, oldStatusId ->
                            viewModel.changeReadingStatus(selectedStatus, bookItemId)
                            changeReadingStatusListener.invoke(oldStatusId, bookItemId)
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