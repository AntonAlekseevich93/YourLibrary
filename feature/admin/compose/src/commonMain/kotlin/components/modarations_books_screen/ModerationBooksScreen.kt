package components.modarations_books_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.elements.BookCover
import main_models.genre.GenreUtils
import models.AdminEvents
import models.ModerationBookState
import tags.CustomTag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BaseEventScope<BaseEvent>.ModerationBooksScreen(
    state: ModerationBookState,
    hazeModifier: Modifier = Modifier,
    topPadding: Dp,
    bottomPadding: Dp,
) {
    val resultBook by remember(state.selectedItem?.id) { mutableStateOf(state.selectedItem) }
    val scrollableState = rememberScrollState()
    val haptic = LocalHapticFeedback.current
    val shortDescription = remember { mutableStateOf(false) }
    val descriptionMaxLines by animateIntAsState(if (shortDescription.value) 3 else 2000)
    LaunchedEffect(state.selectedItem?.imageResultUrl) {
        shortDescription.value = !state.selectedItem?.imageResultUrl.isNullOrEmpty()
    }
    resultBook?.let { book ->
        Column(modifier = hazeModifier.fillMaxSize().verticalScroll(scrollableState)) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = topPadding.plus(24.dp), start = 24.dp)
            ) {
                BookCover(
                    coverUrl = book.rawCoverUrl.orEmpty(),
                    modifier = Modifier
                        .sizeIn(
                            minHeight = 250.dp,
                            minWidth = 160.dp,
                            maxHeight = 250.dp,
                            maxWidth = 160.dp
                        )
                )

                if (!state.selectedItem?.imageResultUrl.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier.padding(start = 16.dp).sizeIn(maxWidth = 150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BookCover(
                            coverUrl = state.selectedItem?.imageResultUrl.orEmpty(),
                            modifier = Modifier
                                .sizeIn(
                                    minHeight = 200.dp,
                                    minWidth = 130.dp,
                                    maxHeight = 200.dp,
                                    maxWidth = 130.dp
                                )
                        )

                        SelectionContainer {
                            Text(
                                text = state.selectedItem?.imageResultUrl.orEmpty(),
                                style = ApplicationTheme.typography.captionRegular,
                                color = ApplicationTheme.colors.mainTextColor,
                                modifier = Modifier.padding(top = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                LazyRow(modifier = Modifier.sizeIn(maxHeight = 400.dp).padding(start = 4.dp)) {
                    itemsIndexed(state.booksForModeration) { index, item ->
                        if (item.id != book.id) {
                            Spacer(Modifier.padding(3.dp))
                            BookCover(coverUrl = item.rawCoverUrl.orEmpty(), modifier = Modifier
                                .sizeIn(
                                    minHeight = 170.dp,
                                    minWidth = 110.dp,
                                    maxHeight = 170.dp,
                                    maxWidth = 110.dp
                                ),
                                onClick = {
                                    if (!state.isUploadingBookImage) {
                                        sendEvent(AdminEvents.SelectBook(item))
                                    }
                                }
                            )
                            Spacer(Modifier.padding(3.dp))
                        }
                    }
                }
            }

            if (state.isUploadingBookImage) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = ApplicationTheme.colors.adminPanelButtons.uploadColor,
                        strokeWidth = 2.dp,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Загружаем картинку на сервер",
                        style = ApplicationTheme.typography.captionRegular,
                        color = ApplicationTheme.colors.adminPanelButtons.uploadColor
                    )
                }

            }

            Row(
                modifier = Modifier.padding(start = 24.dp, top = 16.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Название:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (book.bookName.isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = book.bookName,
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            Row(
                modifier = Modifier.padding(start = 24.dp, top = 16.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Автор:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (book.originalAuthorName.isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = book.originalAuthorName,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            Row(
                modifier = Modifier.padding(start = 24.dp, top = 16.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ServerId:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (book.id.toString().isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = book.id.toString(),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                thickness = 1.dp,
                color = ApplicationTheme.colors.dividerLight
            )

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                val genre = GenreUtils.getGenreById(book.bookGenreId)
                Text(
                    text = "${Strings.genre}:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (genre.name.isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = genre.name,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                Text(
                    text = "Возрастные ограничения:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (book.ageRestrictions.isNullOrEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )
                book.ageRestrictions?.let {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = it,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor
                    )
                }
            }

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                Text(
                    text = "${Strings.pages_title}:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (book.numbersOfPages.toString().isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = book.numbersOfPages.toString(),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                Text(
                    text = "${Strings.isbn}:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = if (book.isbn.isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = book.isbn,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            Text(
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp).clickable {
                    shortDescription.value = !shortDescription.value
                },
                text = book.description,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor,
                maxLines = descriptionMaxLines,
                overflow = TextOverflow.Ellipsis
            )

            if (!state.isUploadingBookImage) {
                FlowRow(
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 84.dp
                    )
                ) { // todo fix bottom padding its for mobile
                    if (!state.selectedItem?.imageResultUrl.isNullOrEmpty()) {
                        CustomTag(
                            modifier = Modifier.padding(top = 12.dp),
                            text = "Одобрено",
                            color = ApplicationTheme.colors.adminPanelButtons.approvedColor,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                HapticFeedbackType.LongPress
                                this@ModerationBooksScreen.sendEvent(AdminEvents.ApprovedBook)
                            }
                        )

                        CustomTag(
                            text = "Одобрено с изменениями",
                            color = ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
                            onClick = {

                            }
                        )
                    } else if (state.canSetBookAsApprovedWithoutUploadImage) {
                        CustomTag(
                            text = "Одобрено без загрузки",
                            color = ApplicationTheme.colors.adminPanelButtons.uploadColor,
                            modifier = Modifier.padding(end = 16.dp, top = 12.dp),
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                HapticFeedbackType.LongPress
                                sendEvent(AdminEvents.SetBookAsApprovedWithoutUploadImage)
                            }
                        )
                    } else {
                        CustomTag(
                            text = "Сохранить обложку в базу данных",
                            color = ApplicationTheme.colors.adminPanelButtons.uploadColor,
                            modifier = Modifier.padding(end = 16.dp, top = 12.dp),
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                sendEvent(AdminEvents.UploadBookCover)
                            }
                        )
                    }

                    CustomTag(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Отказано",
                        color = ApplicationTheme.colors.adminPanelButtons.disapprovedColor,
                        onClick = {
                            this@ModerationBooksScreen.sendEvent(AdminEvents.DiscardBook)
                        }
                    )
                }
            }

            Spacer(Modifier.padding(bottomPadding))
        }
    }
}

