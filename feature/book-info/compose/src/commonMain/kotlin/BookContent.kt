import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import containters.CenterBoxContainer
import main_models.BookVo
import main_models.ReadingStatus
import models.BookScreenEvents
import platform.Platform
import platform.isDesktop
import platform.isMobile
import reading_status.getStatusColor
import tags.CustomTag
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

@Composable
fun BaseEventScope<BaseEvent>.BookContent(
    platform: Platform,
    bookItem: BookVo,
) {
    val url = bookItem.userCoverUrl ?: bookItem.remoteImageLink
    val hasDescriptionTextOverflow = remember { mutableStateOf(false) }
    val showFullDescription = remember { mutableStateOf(false) }
    val maxLines = remember { if (platform.isDesktop()) 8 else 5 }
    val descriptionMaxLines =
        if (showFullDescription.value) mutableStateOf(99) else mutableStateOf(maxLines)
    val scrollableState = rememberScrollState()
    var showReadingStatusSelector by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(
            start = if (platform.isDesktop()) 24.dp else 0.dp,
            end = 10.dp
        ).verticalScroll(scrollableState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            if (platform.isDesktop()) {
                BookCoverWithInfo(
                    platform = platform,
                    bookItem = bookItem,
                    url = url
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                if (platform.isMobile()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BookCoverWithInfo(
                            platform = platform,
                            bookItem = bookItem,
                            url = url
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (platform.isMobile()) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Column(modifier = Modifier.padding(bottom = 12.dp)) {
                        CustomTag(
                            text = bookItem.readingStatus.nameValue,
                            color = bookItem.readingStatus.getStatusColor(),
                            modifier = Modifier.padding(bottom = 8.dp),
                            onClick = {
                                showReadingStatusSelector = true
                            }
                        )

                        ReadingStatusesDropDown(
                            showMenu = showReadingStatusSelector,
                            currentStatus = bookItem.readingStatus,
                            onClose = {
                                showReadingStatusSelector = false
                            },
                            selectedStatusListener = {
                                showReadingStatusSelector = false
                                if (it != bookItem.readingStatus) {
                                    this@BookContent.sendEvent(
                                        BookScreenEvents.ChangeReadingStatusEvent(
                                            it,
                                            bookItem.readingStatus.id
                                        )
                                    )
                                }
                            }
                        )
                    }

                    SelectionContainer {
                        Text(
                            text = bookItem.originalAuthorName,
                            style = ApplicationTheme.typography.buttonRegular,
                            color = ApplicationTheme.colors.mainTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    SelectionContainer {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = bookItem.bookName,
                            style = if (platform.isDesktop()) ApplicationTheme.typography.title2Bold else ApplicationTheme.typography.title3Bold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                    }
                }

                if (platform.isMobile()) {
                    Spacer(Modifier.padding(4.dp))
                    BookInfo(bookItem)
                }

                SelectionContainer {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = bookItem.description,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                        maxLines = descriptionMaxLines.value,
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.hasVisualOverflow) {
                                hasDescriptionTextOverflow.value = true
                            }
                        },
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (hasDescriptionTextOverflow.value) {
                    Text(
                        text = if (showFullDescription.value) Strings.collapse else Strings.expand,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.linkColor,
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                showFullDescription.value = !showFullDescription.value
                            })
                    )
                }
            }
        }
    }
}

@Composable
private fun BookCoverWithInfo(
    platform: Platform,
    bookItem: BookVo,
    url: String?,
) {
    val imageModifier = Modifier.sizeIn(
        minHeight = 260.dp,
        minWidth = 160.dp,
        maxHeight = 260.dp,
        maxWidth = 160.dp
    )
    Column {
        Card(
            modifier = imageModifier,
            shape = RoundedCornerShape(12.dp)
        ) {
            AsyncImage(
                modifier = imageModifier,
                request = ComposableImageRequest(url) {
                    scale(Scale.FILL)
                    placeholder(Res.drawable.ic_default_book_cover_7)
                    error(Res.drawable.ic_default_book_cover_7)
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }

        if (platform.isDesktop()) {
            Spacer(Modifier.padding(8.dp))
            BookInfo(bookItem)
        }
    }
}

@Composable
private fun BookInfo(bookItem: BookVo) {
    SelectionContainer {
        Column {
            if (bookItem.pageCount > 0) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "${bookItem.pageCount} ${Strings.page_short}",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            }
            if (!bookItem.isbn.isNullOrEmpty()) {
                Row {
                    Text(
                        modifier = Modifier,
                        text = "${Strings.isbn}: ",
                        style = ApplicationTheme.typography.footnoteBold,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                    Text(
                        modifier = Modifier,
                        text = bookItem.isbn!!,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReadingStatusesDropDown(
    showMenu: Boolean,
    currentStatus: ReadingStatus,
    onClose: () -> Unit,
    selectedStatusListener: (status: ReadingStatus) -> Unit,
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onClose,
        modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor)
    ) {
        Column {
            CenterBoxContainer {
                Text(
                    text = Strings.change_reading_status,
                    style = ApplicationTheme.typography.footnoteBold,
                    color = ApplicationTheme.colors.mainTextColorLight,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                ReadingStatus.entries.forEachIndexed { index, status ->
                    if (status != currentStatus) {
                        val modifier =
                            if (index == ReadingStatus.entries.size - 1) Modifier
                            else Modifier.padding(end = 12.dp)
                        CustomTag(
                            text = status.nameValue,
                            color = status.getStatusColor(),
                            modifier = modifier,
                            onClick = {
                                selectedStatusListener.invoke(status)
                            }
                        )
                    }
                }
            }
        }
    }
}