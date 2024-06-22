package images

import ApplicationTheme
import Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BookCoverLoadingProcessImage(randomCover: Boolean = true) {
    val coverRes = remember {
        if (randomCover) getRandomCoverRes() else Drawable.drawable_ic_default_book_cover_7
    }

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(DrawableResource(coverRes)),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        CircularProgressIndicator(
            color = Color.White,
            trackColor = ApplicationTheme.colors.mainBackgroundColor
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BookCoverFailureImage(randomCover: Boolean = true) {
    val coverRes = remember {
        if (randomCover) getRandomCoverRes() else Drawable.drawable_ic_default_book_cover_7
    }

    Image(
        painter = painterResource(DrawableResource(coverRes)),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

private fun getRandomCoverRes(): String {
    return when (Random.nextInt(1, 17)) {
        1 -> Drawable.drawable_ic_default_book_cover
        2 -> Drawable.drawable_ic_default_book_cover_2
        3 -> Drawable.drawable_ic_default_book_cover_3
        4 -> Drawable.drawable_ic_default_book_cover_4
        5 -> Drawable.drawable_ic_default_book_cover_5
        6 -> Drawable.drawable_ic_default_book_cover_6
        7 -> Drawable.drawable_ic_default_book_cover_7
        8 -> Drawable.drawable_ic_default_book_cover_8
        9 -> Drawable.drawable_ic_default_book_cover_9
        10 -> Drawable.drawable_ic_default_book_cover_10
        11 -> Drawable.drawable_ic_default_book_cover_11
        12 -> Drawable.drawable_ic_default_book_cover_12
        13 -> Drawable.drawable_ic_default_book_cover_13
        14 -> Drawable.drawable_ic_default_book_cover_14
        15 -> Drawable.drawable_ic_default_book_cover_15
        16 -> Drawable.drawable_ic_default_book_cover_16
        else -> Drawable.drawable_ic_default_book_cover
    }
}
