package components.modarations_books_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.books.BookShortVo
import models.AdminEvents
import tags.CustomTag

@Composable
fun BaseEventScope<BaseEvent>.ModerationBooksScreen(
    booksForModeration: SnapshotStateList<BookShortVo>,
    selectedItem: BookShortVo?,
) {
    val resultBook by remember(selectedItem?.id) { mutableStateOf(selectedItem) }
    val scrollableState = rememberScrollState()
    resultBook?.let { book ->


        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollableState)) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp)
            ) {
                BookCover(
                    book.coverUrl.orEmpty(), modifier = Modifier
                        .sizeIn(
                            minHeight = 250.dp,
                            minWidth = 180.dp,
                            maxHeight = 280.dp,
                            maxWidth = 180.dp
                        )
                )

                LazyRow(modifier = Modifier.sizeIn(maxHeight = 400.dp).padding(start = 4.dp)) {
                    itemsIndexed(booksForModeration) { index, item ->
                        if (item.id != book.id) {
                            BookCover(coverUrl = item.coverUrl.orEmpty(), modifier = Modifier
                                .sizeIn(
                                    minHeight = 165.dp,
                                    minWidth = 130.dp,
                                    maxHeight = 165.dp,
                                    maxWidth = 130.dp
                                ).padding(horizontal = 12.dp),
                                onClick = {
                                    sendEvent(AdminEvents.SelectBook(item))
                                }
                            )
                        }
                    }
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

            Divider(
                thickness = 1.dp,
                color = ApplicationTheme.colors.dividerLight,
                modifier = Modifier.padding(vertical = 18.dp)
            )

            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = book.bookGenreName,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor
            )

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                Text(
                    text = "Страниц:",
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
                    text = "ISBN:",
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

            Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp)) {
                CustomTag(
                    text = "Одобрено",
                    color = Color.Green,
                    onClick = {
                        this@ModerationBooksScreen.sendEvent(AdminEvents.ApprovedBook)
                    }
                )

                CustomTag(
                    text = "Одобрено с изменениями",
                    color = Color.Blue,
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {

                    }
                )

                CustomTag(
                    text = "Отказано",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {

                    }
                )
            }
        }
    }
}

@Composable
internal fun BookCover(
    coverUrl: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val painter = asyncPainterResource(
        data = coverUrl,
        key = coverUrl
    )
    when (painter) {
        is Resource.Loading -> {

        }

        is Resource.Success -> {

        }

        is Resource.Failure -> {

        }
    }

    Card(
        modifier = modifier.clickable {
            onClick?.invoke()
        },
        colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        KamelImage(
            resource = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            onFailure = {
                //todo
            },
            onLoading = {
                //todo
            },
        )
    }
}

