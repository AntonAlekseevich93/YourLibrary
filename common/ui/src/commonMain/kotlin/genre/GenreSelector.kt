package genre

import ApplicationTheme
import Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import main_models.genre.Genre
import main_models.genre.GenreUtils
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val genres = GenreUtils

@Composable
fun GenreSelector(modifier: Modifier = Modifier, selectedItemCallback: (item: Genre) -> Unit) {
    val scrollableState = rememberScrollState()
    Column(modifier = modifier.fillMaxWidth().verticalScroll(scrollableState)) {
        val mainList = remember { genres.getAllMainGenres() }
        for (item in mainList) {
            GenreItem(
                item,
                modifier = Modifier.padding(vertical = 4.dp),
                selectedItemCallback = selectedItemCallback
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GenreItem(
    genre: Genre,
    modifier: Modifier = Modifier,
    selectedItemCallback: (item: Genre) -> Unit
) {
    val isOpen = remember { mutableStateOf(false) }
    val hasInnerGenre = remember { genres.hasInnerGenre(genre.id) }
    val interactionSource = remember { MutableInteractionSource() }
    val style =
        if (hasInnerGenre) ApplicationTheme.typography.bodyRegular.copy(textDecoration = TextDecoration.Underline)
        else ApplicationTheme.typography.bodyRegular

    Column(modifier = modifier.clickable(interactionSource = interactionSource, indication = null) {
        if (hasInnerGenre) {
            isOpen.value = !isOpen.value
        } else {
            selectedItemCallback(genre)
        }
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier,
                text = genre.name,
                style = style,
                color = ApplicationTheme.colors.mainTextColor
            )
            if (hasInnerGenre) {
                Image(
                    painter = if (isOpen.value) painterResource(DrawableResource(Drawable.drawable_ic_arrow_down))
                    else painterResource(DrawableResource(Drawable.drawable_ic_arrow_right)),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.screenColor.iconColor),
                    modifier = Modifier.padding(start = 8.dp).size(18.dp)
                )
            }
        }
        if (isOpen.value) {
            Column(modifier = Modifier.padding(start = 10.dp)) {
                val items = genres.getAllGenresForSelectedGenre(genre.id)
                for (item in items) {
                    GenreItem(
                        genre = item,
                        modifier = Modifier.padding(start = 10.dp).padding(vertical = 4.dp),
                        selectedItemCallback = selectedItemCallback
                    )
                }
            }
        }
    }
}
