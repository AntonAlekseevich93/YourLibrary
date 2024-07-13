package components.modarations_books_screen.elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

@Composable
internal fun BookCover(
    coverUrl: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.clickable {
            onClick?.invoke()
        },
        colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            AsyncImage(
                modifier = modifier,
                request = ComposableImageRequest(coverUrl) {
                    scale(Scale.FILL)
                    placeholder(Res.drawable.ic_default_book_cover_7)
                    error(Res.drawable.ic_default_book_cover_7)
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }
    }
}

