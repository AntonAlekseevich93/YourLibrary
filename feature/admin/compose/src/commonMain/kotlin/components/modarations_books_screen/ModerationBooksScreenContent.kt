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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.elements.BookCover
import main_models.genre.GenreUtils
import models.AdminEvents
import models.AdminUiState
import tags.CustomTag
import text_fields.CommonTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BaseEventScope<BaseEvent>.ModerationBooksScreenContent(
    uiState: AdminUiState,
    topPadding: Dp,
) {
    val resultBook by remember(uiState.moderationBookState.selectedItem?.id) {
        mutableStateOf(
            uiState.moderationBookState.selectedItem
        )
    }

    val haptic = LocalHapticFeedback.current
    val shortDescription = remember { mutableStateOf(false) }
    val descriptionMaxLines by animateIntAsState(if (shortDescription.value) 3 else 2000)
    LaunchedEffect(uiState.moderationBookState.selectedItem?.imageResultUrl) {
        shortDescription.value =
            !uiState.moderationBookState.selectedItem?.imageResultUrl.isNullOrEmpty()
    }
    resultBook?.let { book ->

        Row(//todo remove
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = topPadding.plus(24.dp), start = 16.dp)
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

            if (!uiState.moderationBookState.selectedItem?.imageResultUrl.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.padding(start = 16.dp).sizeIn(maxWidth = 150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BookCover(
                        coverUrl = uiState.moderationBookState.selectedItem?.imageResultUrl.orEmpty(),
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
                            text = uiState.moderationBookState.selectedItem?.imageResultUrl.orEmpty(),
                            style = ApplicationTheme.typography.captionRegular,
                            color = ApplicationTheme.colors.mainTextColor,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            LazyRow(modifier = Modifier.sizeIn(maxHeight = 400.dp).padding(start = 4.dp)) {
                itemsIndexed(uiState.moderationBookState.booksForModeration) { index, item ->
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
                                if (!uiState.moderationBookState.isUploadingBookImage) {
                                    sendEvent(AdminEvents.SelectBook(item))
                                }
                            }
                        )
                        Spacer(Modifier.padding(3.dp))
                    }
                }
            }
        }

        //todo remove
        if (uiState.moderationBookState.isUploadingBookImage) {
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
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Название:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (book.bookName.isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.clickable {
                    sendEvent(AdminEvents.OnChangeBookName)
                }.padding(start = 8.dp),
                text = book.bookName,
                style = ApplicationTheme.typography.bodyBold,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        if (uiState.moderationBookState.moderationChangedName.value != null) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Измененное название:",
                    style = ApplicationTheme.typography.bodyBold,
                    color = if (uiState.moderationBookState.moderationChangedName.value!!.isEmpty()) {
                        ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                    } else {
                        ApplicationTheme.colors.adminPanelButtons.uploadColor
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = uiState.moderationBookState.moderationChangedName.value!!,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }
        }

        if (uiState.moderationBookState.showChangedBookNameField.value) {
            val textField: MutableState<TextFieldValue> =
                remember {
                    mutableStateOf(
                        TextFieldValue(
                            text = uiState.moderationBookState.moderationChangedName.value
                                ?: book.bookName
                        )
                    )
                }
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
                CommonTextField(
                    modifier = Modifier,
                    focusedIndicatorLineThickness = 1.dp,
                    unfocusedIndicatorLineThickness = 1.dp,
                    textState = textField.value,
                    onTextChanged = {
                        textField.value = it
                    },
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(
                        text = "Сохранить",
                        modifier = Modifier.clickable {
                            sendEvent(AdminEvents.OnSaveChangeBookName(textField.value.text.trim()))
                        }.padding(end = 12.dp),
                        style = ApplicationTheme.typography.footnoteBold,
                        color = ApplicationTheme.colors.mainTextColor
                    )

                    Text(
                        text = "Отменить",
                        modifier = Modifier.clickable {
                            sendEvent(AdminEvents.OnCancelChangeBookName)
                        }.padding(end = 12.dp),
                        style = ApplicationTheme.typography.footnoteBold,
                        color = ApplicationTheme.colors.mainTextColor
                    )

                    Text(
                        text = "Удалить",
                        modifier = Modifier.clickable {
                            sendEvent(AdminEvents.OnDeleteChangeBookName)
                        },
                        style = ApplicationTheme.typography.footnoteBold,
                        color = ApplicationTheme.colors.mainTextColor
                    )
                }

            }

        }

        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Автор:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (book.originalAuthorName.isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = book.originalAuthorName,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ServerId:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (book.id.toString().isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = book.id.toString(),
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 18.dp),
            thickness = 1.dp,
            color = ApplicationTheme.colors.dividerLight
        )

        Row(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
            val genre = GenreUtils.getGenreById(book.bookGenreId)
            Text(
                text = "${Strings.genre}:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (genre.name.isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = genre.name,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        Row(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
            Text(
                text = "Возрастные ограничения:",
                style = ApplicationTheme.typography.bodyBold,
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
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }
        }

        Row(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
            Text(
                text = "${Strings.pages_title}:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (book.numbersOfPages.toString().isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = book.numbersOfPages.toString(),
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        Row(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
            Text(
                text = "${Strings.isbn}:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (book.isbn.isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = book.isbn,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp).clickable {
                shortDescription.value = !shortDescription.value
            },
            text = book.description,
            style = ApplicationTheme.typography.bodyRegular.copy(
                textDirection = TextDirection.Content,
                hyphens = Hyphens.Auto,
                lineBreak = LineBreak.Paragraph
            ),
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = descriptionMaxLines,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            softWrap = true,
            letterSpacing = TextUnit.Unspecified,
        )

        if (!uiState.moderationBookState.isUploadingBookImage) {
            FlowRow(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 84.dp
                )
            ) { // todo fix bottom padding its for mobile
                if (!uiState.moderationBookState.selectedItem?.imageResultUrl.isNullOrEmpty()) {
                    CustomTag(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Одобрено",
                        color = ApplicationTheme.colors.adminPanelButtons.approvedColor,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            HapticFeedbackType.LongPress
                            sendEvent(AdminEvents.ApprovedBook)
                        }
                    )

                    CustomTag(
                        text = "Одобрено с изменениями",
                        color = ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
                        onClick = {

                        }
                    )
                } else if (uiState.moderationBookState.canSetBookAsApprovedWithoutUploadImage) {
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
                        sendEvent(AdminEvents.DiscardBook)
                    }
                )
            }
        }

        Spacer(Modifier.padding(16.dp))
    }
}