package components.modarations_books_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.elements.AgeRestrictionsElement
import components.modarations_books_screen.elements.AuthorElement
import components.modarations_books_screen.elements.BookCover
import components.modarations_books_screen.elements.BookNameElement
import components.modarations_books_screen.elements.ChangeBookNameEditorElement
import components.modarations_books_screen.elements.ChangedBookNameElement
import components.modarations_books_screen.elements.GenreElement
import components.modarations_books_screen.elements.IsbnElement
import components.modarations_books_screen.elements.PagesElement
import components.modarations_books_screen.elements.ServerIdElement
import containters.CenterBoxContainer
import models.AdminEvents
import models.AdminUiState
import tags.CustomTag

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
    var shortView by remember { mutableStateOf(true) }
    LaunchedEffect(uiState.moderationBookState.selectedItem?.imageResultUrl) {
        shortDescription.value =
            !uiState.moderationBookState.selectedItem?.imageResultUrl.isNullOrEmpty()
    }
    resultBook?.let { book ->
        Row(
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
                    text = "Одобряем...",
                    style = ApplicationTheme.typography.captionRegular,
                    color = ApplicationTheme.colors.adminPanelButtons.uploadColor
                )
            }

        }

        BookNameElement(book.bookName)

        if (uiState.moderationBookState.moderationChangedName.value != null) {
            ChangedBookNameElement(changedName = uiState.moderationBookState.moderationChangedName.value!!)
        }

        if (uiState.moderationBookState.showChangedBookNameField.value) {
            ChangeBookNameEditorElement(
                changedName = uiState.moderationBookState.moderationChangedName.value
                    ?: book.bookName
            )

        }

        AuthorElement(authorName = book.originalAuthorName, shortView = shortView)

        ServerIdElement(serverId = book.id.toString(), shortView = shortView)

        if (!shortView) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                thickness = 1.dp,
                color = ApplicationTheme.colors.dividerLight
            )
        }


        CenterBoxContainer {
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        shortView = !shortView
                    }
                    .size(22.dp)
                    .graphicsLayer {
                        rotationZ = if (shortView) 0f else 180f
                    }
            )
        }


        GenreElement(genreId = book.bookGenreId, shortView = shortView)


        AgeRestrictionsElement(ageRestriction = book.ageRestrictions, shortView = shortView)

        PagesElement(pages = book.numbersOfPages.toString(), shortView = shortView)

        IsbnElement(isbn = book.isbn, shortView = shortView)

        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = if (shortView) 0.dp else 12.dp,
                end = 16.dp
            ).clickable {
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

        if (!uiState.moderationBookState.isUploadingBookImage && uiState.moderationBookState.booksForModeration.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 84.dp
                )
            ) {
                CustomTag(
                    text = "Одобрить текущую книгу",
                    color = ApplicationTheme.colors.adminPanelButtons.uploadColor,
                    modifier = Modifier.padding(end = 16.dp, top = 12.dp),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        HapticFeedbackType.LongPress
                        sendEvent(AdminEvents.SetBookAsApprovedWithoutUploadImage)
                    }
                )

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