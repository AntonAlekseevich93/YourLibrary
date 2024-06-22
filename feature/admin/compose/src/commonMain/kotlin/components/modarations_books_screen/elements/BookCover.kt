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
import images.BookCoverFailureImage
import images.BookCoverLoadingProcessImage
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

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

    Card(
        modifier = modifier.clickable {
            onClick?.invoke()
        },
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
            when (painter) {
                is Resource.Loading -> {
                    BookCoverLoadingProcessImage(randomCover = false)
                }

                is Resource.Success -> {

                }

                is Resource.Failure -> {
                    BookCoverFailureImage()
                }
            }
        }
    }
}

