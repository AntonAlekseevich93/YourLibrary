import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.BookItemVo
import models.BookItemCardConfig

@Composable
fun BookItemCard(
    config: BookItemCardConfig,
    bookItem: BookItemVo,
    openBookListener: () -> Unit
) {
    val url =
        if (bookItem.coverUrlFromParsing.isNotEmpty()) bookItem.coverUrlFromParsing else bookItem.coverUrl
    val painter =
        asyncPainterResource(data = url)

    Column(
        modifier = Modifier
            .sizeIn(maxWidth = config.width.dp)
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        Card(
            modifier = Modifier.size(width = config.width.dp, height = config.height.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Box {
                KamelImage(
                    resource = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        Text(
            text = bookItem.authorName,
            modifier = Modifier
                .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                .sizeIn(maxWidth = config.width.dp),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = bookItem.bookName,
            modifier = Modifier
                .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                .sizeIn(maxWidth = config.width.dp),
            style = ApplicationTheme.typography.footnoteBold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}