package book_editor.elements.book_selector.elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.books.BookShortVo
import reading_status.getStatusColor

private val MAX_ITEM_WITH = 130.dp

@Composable
fun BookSelectorItem(
    bookItem: BookShortVo,
    modifier: Modifier = Modifier,
    onClick: (book: BookShortVo) -> Unit,
    bookHaveReadingStatusEvent: () -> Unit,
) {
    val painter = asyncPainterResource(
        data = bookItem.imageResultUrl,
        key = bookItem.imageResultUrl
    )
    when (painter) {
        is Resource.Loading -> {

        }

        is Resource.Success -> {
        }

        is Resource.Failure -> {
        }
    }
    Column(
        modifier = modifier
            .clickable(interactionSource = MutableInteractionSource(), null) {
                if (bookItem.readingStatus == null) {
                    onClick(bookItem)
                } else {
                    bookHaveReadingStatusEvent()
                }
            }
            .sizeIn(maxWidth = MAX_ITEM_WITH),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .sizeIn(
                    minHeight = 200.dp,
                    minWidth = MAX_ITEM_WITH,
                    maxHeight = 200.dp,
                    maxWidth = MAX_ITEM_WITH
                ),
            colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box {
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
                bookItem.readingStatus?.let { readingStatus ->
                    Column(Modifier.align(Alignment.TopStart)) {
                        Card(
                            shape = RoundedCornerShape(bottomEnd = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = ApplicationTheme.colors.mainBackgroundColor.copy(
                                    alpha = 0.7f
                                )
                            ),
                        ) {
                            Text(
                                text = readingStatus.nameValue,
                                style = ApplicationTheme.typography.footnoteBold,
                                color = readingStatus.getStatusColor(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = bookItem.bookName,
            style = ApplicationTheme.typography.footnoteBold,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = bookItem.originalAuthorName,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}