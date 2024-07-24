package main_app_bar

import ApplicationTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import main_models.BookVo
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

@Composable
fun SearchAppBarResultInfo(books: List<BookVo>) {
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        Spacer(Modifier.padding(top = 16.dp))
        books.fastForEach {
            SearchAppBarInfoItem(it)
            Spacer(Modifier.padding(bottom = 8.dp))
        }
    }
}

@Composable
private fun SearchAppBarInfoItem(book: BookVo) {
    val imageModifier = Modifier
        .sizeIn(
            minHeight = 70.dp,
            minWidth = 55.dp,
            maxHeight = 70.dp,
            maxWidth = 55.dp
        )
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Card(
            modifier = imageModifier.padding(start = 16.dp),
            colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
            shape = RoundedCornerShape(6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box {
                AsyncImage(
                    modifier = imageModifier,
                    request = ComposableImageRequest(book.remoteImageLink) {
                        scale(Scale.FILL)
                        placeholder(Res.drawable.ic_default_book_cover_7)
                        error(Res.drawable.ic_default_book_cover_7)
                    },
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )
            }
        }
        Column(Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = book.bookName,
                modifier = Modifier,
                style = ApplicationTheme.typography.footnoteBold,
                color = ApplicationTheme.colors.mainTextColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
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
}
