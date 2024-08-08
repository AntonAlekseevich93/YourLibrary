import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import main_models.BookVo
import models.BookItemCardConfig
import navigation_drawer.contents.models.DrawerEvents
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

@Composable
fun BaseEventScope<BaseEvent>.BookItemShelfCard(
    config: BookItemCardConfig,
    bookItem: BookVo,
) {
    val url = bookItem.userCoverUrl ?: bookItem.remoteImageLink

    Column(
        modifier = Modifier
            .sizeIn(maxWidth = config.width.dp)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(width = config.width.dp, height = config.height.dp)
                .clickable {
                    bookItem.localId?.let {
                        this@BookItemShelfCard.sendEvent(DrawerEvents.OpenBook(it))
                    }
                },
            shape = RoundedCornerShape(8.dp),
        ) {
            AsyncImage(
                modifier = Modifier.size(width = config.width.dp, height = config.height.dp),
                request = ComposableImageRequest(url) {
                    scale(Scale.FILL)
                    placeholder(Res.drawable.ic_default_book_cover_7)
                    error(Res.drawable.ic_default_book_cover_7)
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }
        Text(
            text = bookItem.bookName,
            modifier = Modifier
                .padding(top = 8.dp)
                .sizeIn(maxWidth = config.width.dp),
            style = ApplicationTheme.typography.footnoteBold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = bookItem.originalAuthorName,
            modifier = Modifier
                .padding(top = 8.dp)
                .sizeIn(maxWidth = config.width.dp),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}