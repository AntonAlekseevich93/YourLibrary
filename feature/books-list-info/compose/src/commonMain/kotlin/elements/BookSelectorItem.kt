package elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import main_models.books.BookShortVo
import main_models.genre.GenreUtils
import rating.CurrentUserRatingLabel
import reading_status.getStatusColor
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

private val MAX_ITEM_WITH = 85.dp

@Composable
fun BookSelectorItem(
    bookItem: BookShortVo,
    modifier: Modifier = Modifier,
    maxLinesBookName: Int = Int.MAX_VALUE,
    maxLinesAuthorName: Int = Int.MAX_VALUE,
    onClick: (book: BookShortVo) -> Unit,
    changeBookReadingStatus: (bookId: String) -> Unit,
) {
    Column {
        bookItem.readingStatus?.let { readingStatus ->
            Card(
                colors = CardDefaults.cardColors(containerColor = readingStatus.getStatusColor()),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = readingStatus.nameValue,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainBackgroundColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Row(
            modifier = modifier
                .padding(start = 8.dp)
                .clickable(interactionSource = MutableInteractionSource(), null) {
                    onClick(bookItem)
                },
            verticalAlignment = Alignment.Top
        ) {
            val imageModifier = Modifier.sizeIn(
                minHeight = 140.dp,
                minWidth = MAX_ITEM_WITH,
                maxHeight = 140.dp,
                maxWidth = MAX_ITEM_WITH
            )
            Card(
                modifier = imageModifier,
                colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box {
                    AsyncImage(
                        modifier = imageModifier,
                        request = ComposableImageRequest(bookItem.imageResultUrl) {
                            scale(Scale.FILL)
                            placeholder(Res.drawable.ic_default_book_cover_7)
                            error(Res.drawable.ic_default_book_cover_7)
                        },
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 16.dp, top = 2.dp)
                    .weight(1f)
            ) {
                Text(
                    text = bookItem.bookName,
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(bottom = 8.dp),
                    maxLines = maxLinesBookName,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = bookItem.originalAuthorName,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(bottom = 10.dp),
                    maxLines = maxLinesAuthorName,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = GenreUtils.getGenreById(bookItem.bookGenreId).name,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (bookItem.ratingValue > 0 && bookItem.ratingCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Color(0xFFfaa307),
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = bookItem.ratingValue.toString(),
                            style = ApplicationTheme.typography.footnoteRegular,
                            color = ApplicationTheme.colors.mainTextColor,
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        Text(
                            text = "(${bookItem.ratingCount})",
                            style = ApplicationTheme.typography.footnoteRegular,
                            color = ApplicationTheme.colors.hintColor,
                            modifier = Modifier.padding()
                        )
                        bookItem.currentUserRating?.let { userRating ->
                            CurrentUserRatingLabel(
                                rating = userRating.ratingScore,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .height(140.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        changeBookReadingStatus(bookItem.bookId)
                    }
            ) {
                Spacer(Modifier.padding(start = 10.dp))
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = ApplicationTheme.colors.mainIconsColor,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    }
}