package images

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_10
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_11
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_12
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_13
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_14
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_15
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_16
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_2
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_3
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_4
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_5
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_6
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_8
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_9
import kotlin.random.Random

@Composable
fun BookCoverLoadingProcessImage(modifier: Modifier = Modifier, randomCover: Boolean = true) {
    val coverRes = remember {
        if (randomCover) getRandomCoverRes() else Res.drawable.ic_default_book_cover_7
    }

    Box(contentAlignment = Alignment.Center) {
        Image(
            modifier = modifier,
            painter = painterResource(coverRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        CircularProgressIndicator(
            color = Color.White,
            trackColor = ApplicationTheme.colors.mainBackgroundColor
        )
    }
}

@Composable
fun BookCoverFailureImage(modifier: Modifier = Modifier, randomCover: Boolean = true) {
    val coverRes = remember {
        if (randomCover) getRandomCoverRes() else Res.drawable.ic_default_book_cover_7
    }

    Image(
        modifier = modifier,
        painter = painterResource(coverRes),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

fun getRandomCoverRes(): DrawableResource {
    return when (Random.nextInt(1, 17)) {
        1 -> Res.drawable.ic_default_book_cover
        2 -> Res.drawable.ic_default_book_cover_2
        3 -> Res.drawable.ic_default_book_cover_3
        4 -> Res.drawable.ic_default_book_cover_4
        5 -> Res.drawable.ic_default_book_cover_5
        6 -> Res.drawable.ic_default_book_cover_6
        7 -> Res.drawable.ic_default_book_cover_7
        8 -> Res.drawable.ic_default_book_cover_8
        9 -> Res.drawable.ic_default_book_cover_9
        10 -> Res.drawable.ic_default_book_cover_10
        11 -> Res.drawable.ic_default_book_cover_11
        12 -> Res.drawable.ic_default_book_cover_12
        13 -> Res.drawable.ic_default_book_cover_13
        14 -> Res.drawable.ic_default_book_cover_14
        15 -> Res.drawable.ic_default_book_cover_15
        16 -> Res.drawable.ic_default_book_cover_16
        else -> Res.drawable.ic_default_book_cover
    }
}
