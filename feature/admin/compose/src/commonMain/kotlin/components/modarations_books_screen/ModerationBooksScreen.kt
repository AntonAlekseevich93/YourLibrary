package components.modarations_books_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.elements.BookCover
import models.AdminEvents
import models.ModerationBookState
import tags.CustomTag

@Composable
fun BaseEventScope<BaseEvent>.ModerationBooksScreen(
    state: ModerationBookState
) {
    val resultBook by remember(state.selectedItem?.id) { mutableStateOf(state.selectedItem) }
    val scrollableState = rememberScrollState()
    resultBook?.let { book ->
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollableState)) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp)
            ) {
                BookCover(
                    coverUrl = book.coverUrl.orEmpty(),
                    modifier = Modifier
                        .sizeIn(
                            minHeight = 250.dp,
                            minWidth = 180.dp,
                            maxHeight = 280.dp,
                            maxWidth = 180.dp
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
                            BookCover(coverUrl = item.coverUrl.orEmpty(), modifier = Modifier
                                .sizeIn(
                                    minHeight = 165.dp,
                                    minWidth = 130.dp,
                                    maxHeight = 165.dp,
                                    maxWidth = 130.dp
                                ).padding(horizontal = 12.dp),
                                onClick = {
                                    if (!state.isUploadingBookImage) {
                                        sendEvent(AdminEvents.SelectBook(item))
                                    }
                                }
                            )
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


            Text(
                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp, top = 16.dp),
                text = book.bookName,
                style = ApplicationTheme.typography.bodyBold,
                color = ApplicationTheme.colors.mainTextColor
            )

            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = book.originalAuthorName,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                thickness = 1.dp,
                color = ApplicationTheme.colors.dividerLight
            )

            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = book.bookGenreName,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor
            )

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                Text(
                    text = "${Strings.pages_title}:",
                    style = ApplicationTheme.typography.footnoteBold,
                    color = ApplicationTheme.colors.mainTextColor
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
                    color = ApplicationTheme.colors.mainTextColor
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = book.isbn,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }

            Text(
                modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp),
                text = book.description,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor
            )

            if (!state.isUploadingBookImage) {
                Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                    if (!state.selectedItem?.imageResultUrl.isNullOrEmpty()) {
                        CustomTag(
                            text = "Одобрено",
                            color = ApplicationTheme.colors.adminPanelButtons.approvedColor,
                            onClick = {
                                this@ModerationBooksScreen.sendEvent(AdminEvents.ApprovedBook)
                            }
                        )

                        CustomTag(
                            text = "Одобрено с изменениями",
                            color = ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            onClick = {

                            }
                        )
                    } else {
                        CustomTag(
                            text = "Сохранить обложку в базу данных",
                            color = ApplicationTheme.colors.adminPanelButtons.uploadColor,
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = {
                                sendEvent(AdminEvents.UploadBookCover)
                            }
                        )
                    }

                    CustomTag(
                        text = "Отказано",
                        color = ApplicationTheme.colors.adminPanelButtons.disapprovedColor,
                        onClick = {

                        }
                    )
                }
            }

        }
    }
}

