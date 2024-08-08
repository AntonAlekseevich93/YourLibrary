package book_info

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import main_models.books.BookShortVo
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7
import yourlibrary.common.resources.generated.resources.show_all_books_with_new_line

@Composable
fun BooksHorizontalSlider(
    books: State<List<BookShortVo>>,
    allBooksCount: Int,
    modifier: Modifier = Modifier,
    maxBooks: Int = 10,
    showAllBooksListener: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val with = remember { 125.dp }
    val height = remember { 200.dp }
    val imageModifier = Modifier
        .sizeIn(
            minHeight = height,
            minWidth = with,
            maxHeight = height,
            maxWidth = with
        )
    Row(
        modifier = modifier.horizontalScroll(scrollState).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        books.value.fastForEachIndexed { index, book ->
            if (index + 1 > maxBooks) {
                return@fastForEachIndexed
            }
            HorizontalSliderBookItem(
                book,
                modifier = modifier.padding(end = 20.dp),
                imageModifier = imageModifier,
                with = with
            )
        }
        if (allBooksCount > maxBooks) {
            Column(
                modifier = Modifier.sizeIn(minHeight = height + 18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.show_all_books_with_new_line),
                    style = ApplicationTheme.typography.footnoteBold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(start = 26.dp, end = 20.dp)
                        .clickable(interactionSource = MutableInteractionSource(), null) {
                            showAllBooksListener()
                        }
                )
            }
        }
    }
}

@Composable
fun HorizontalSliderBookItem(
    book: BookShortVo,
    imageModifier: Modifier,
    with: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.sizeIn(maxWidth = with),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = imageModifier,
            colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
            shape = RoundedCornerShape(6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            AsyncImage(
                modifier = imageModifier,
                request = ComposableImageRequest(book.imageResultUrl) {
                    scale(Scale.FILL)
                    placeholder(Res.drawable.ic_default_book_cover_7)
                    error(Res.drawable.ic_default_book_cover_7)
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }

        Text(
            text = book.bookName,
            modifier = Modifier.padding(top = 12.dp),
            style = ApplicationTheme.typography.footnoteBold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = book.originalAuthorName,
            modifier = Modifier
                .padding(top = 8.dp),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}