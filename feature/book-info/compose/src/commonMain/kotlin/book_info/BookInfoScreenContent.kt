package book_info

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.launch
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.genre.GenreUtils
import models.BookInfoUiState
import models.BookScreenEvents
import rating.ReadingStatusSelectorDialog
import reading_status.getStatusColor
import review.WriteReviewScreen
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BaseEventScope<BaseEvent>.BookInfoScreenContent(
    uiState: BookInfoUiState,
    bookShortVo: BookShortVo?,
    bookName: State<String>,
) {
    val hazeState = remember { HazeState() }
    val height = remember { 520.dp }
    val heigh2 = remember { 240.dp }
    val height3 = remember { 231.dp }
    var showDialog by remember { mutableStateOf(false) }
    var showWriteReviewBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    val bookAuthor: String by remember(
        key1 = uiState.bookItem.value,
        key2 = bookShortVo
    ) {
        mutableStateOf(
            bookShortVo?.originalAuthorName
                ?: uiState.bookItem.value?.originalAuthorName.orEmpty()
        )
    }

    val imageUrl: String by remember(
        key1 = uiState.bookItem.value,
        key2 = bookShortVo
    ) {
        mutableStateOf(
            bookShortVo?.imageResultUrl ?: uiState.bookItem.value?.remoteImageLink.orEmpty()
        )
    }

    val readingStatus: ReadingStatus? by remember(
        key1 = uiState.bookItem.value,
        key2 = bookShortVo
    ) {
        mutableStateOf(
            bookShortVo?.readingStatus
                ?: uiState.bookItem.value?.readingStatus
        )
    }

    Box(
        modifier = Modifier.height(height).fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val imageModifier = Modifier.sizeIn(
            minHeight = 230.dp,
            minWidth = 150.dp,
            maxHeight = 230.dp,
            maxWidth = 150.dp
        )

        Box(Modifier) {
            AsyncImage(
                modifier = Modifier.height(height).fillMaxWidth().haze(
                    state = hazeState,
                    style = HazeStyle(
                        tint = Color.Black.copy(alpha = .2f),
                        blurRadius = 30.dp,
                    )
                ),
                request = ComposableImageRequest(imageUrl) {
                    scale(Scale.FILL)
                    placeholder(Res.drawable.ic_default_book_cover_7)
                    error(Res.drawable.ic_default_book_cover_7)
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
            Box(
                modifier = Modifier.height(height).fillMaxWidth().hazeChild(
                    state = hazeState,
                )
            )
        }

        Column(
            modifier = Modifier.padding(top = 110.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = imageModifier,
                colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AsyncImage(
                    modifier = imageModifier,
                    request = ComposableImageRequest(imageUrl) {
                        scale(Scale.FILL)
                        placeholder(Res.drawable.ic_default_book_cover_7)
                        error(Res.drawable.ic_default_book_cover_7)
                    },
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )
            }

            Text(
                text = bookName.value,
                style = ApplicationTheme.typography.bodyBold,
                color = ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.padding(
                    bottom = 12.dp,
                    top = 18.dp,
                    start = 32.dp,
                    end = 32.dp
                ),
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = bookAuthor,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp
                ),
            )
        }
    }

    Column(Modifier.background(Color.Transparent).fillMaxWidth().fillMaxHeight()) {
        Spacer(Modifier.padding(heigh2))
        Column(
            modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(ApplicationTheme.colors.cardBackgroundDark)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.padding(top = 60.dp))
        }
    }

    readingStatus?.let { status ->
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.padding(height3))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = status.getStatusColor()
                ),
                modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                ) {
                    showDialog = true
                }
            ) {
                Text(
                    text = status.nameValue,
                    style = ApplicationTheme.typography.headlineMedium,
                    color = ApplicationTheme.colors.mainBackgroundColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            uiState.bookItem.value?.let { book ->
                BookInfoAboutBook(
                    description = book.description,
                    genre = GenreUtils.getGenreById(book.bookGenreId).name,
                    pageCount = book.pageCount,
                    startDate = book.startDateInString,
                    endDate = book.endDateInString,
                    readingDayAmount = book.getReadingDays(),
                    ageRestrictions = book.ageRestrictions,
                    currentUserScore = 0,//todo
                    allUsersRating = book.ratingValue,
                    allRatingAmount = book.ratingCount,
                    userReview = "Отличная книга. Прочитал с полным удовольствием.",
                    otherBooksByAuthor = uiState.otherBooksByAuthor,
                    onWriteReviewListener = {
                        showWriteReviewBottomSheet = true
                    },
                )
            }

        }
    }

    if (showWriteReviewBottomSheet) {
        Column(modifier = Modifier.fillMaxSize()) {
            ModalBottomSheet(
                sheetState = sheetState,
                windowInsets = WindowInsets(top = 58.dp),
                containerColor = ApplicationTheme.colors.cardBackgroundLight,
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showWriteReviewBottomSheet = false
                        }
                    }
                },
                dragHandle = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BottomSheetDefaults.DragHandle(color = ApplicationTheme.colors.hintColor)
                    }
                },
                scrimColor = Color.Transparent
            ) {
                WriteReviewScreen(
                    bookName = bookName.value,
                    userRating = 0,//todo
                    modifier = Modifier
                )
            }
        }
    }

    if (showDialog && readingStatus != null) {
        ReadingStatusSelectorDialog(
            currentStatus = readingStatus!!,
            selectStatusListener = {
                showDialog = false
                sendEvent(
                    BookScreenEvents.ChangeReadingStatusEvent(
                        selectedStatus = it,
                        bookId = bookShortVo?.bookId
                            ?: uiState.bookItem.value?.bookId.orEmpty()
                    )
                )
            },
            dismiss = { showDialog = false }
        )
    }
}