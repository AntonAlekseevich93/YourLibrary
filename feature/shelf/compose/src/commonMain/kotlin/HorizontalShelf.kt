import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import main_models.ShelfVo
import models.BookItemCardConfig
import models.ShelfEvents
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.HorizontalShelfScreen(
    shelfVo: ShelfVo,
    config: BookItemCardConfig,
    index: Int,
) {
    val firstElements =
        remember(shelfVo.booksList) { shelfVo.booksList.take(config.maxItemsInHorizontalShelf) }
    val state = rememberLazyListState()

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 10.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        this@HorizontalShelfScreen.sendEvent(ShelfEvents.ExpandShelfEvent(index))
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = shelfVo.name,
                modifier = Modifier,
                style = ApplicationTheme.typography.title1Bold,
                color = ApplicationTheme.colors.mainTextColor,
            )
            Image(
                painter = painterResource(DrawableResource(Drawable.drawable_ic_expand_shape)),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp).size(16.dp)
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor
            )
        ) {
            LazyRow(modifier = Modifier, state = state) {
                items(firstElements) { bookItem ->
                    BookItemShelfCard(
                        config = config,
                        bookItem = bookItem,
                    )
                }
                item {
                    Card(
                        modifier = Modifier.size(
                            width = config.width.dp,
                            height = config.height.dp + 24.dp
                        ).padding(
                            vertical = 12.dp,
                            horizontal = 12.dp
                        ).clickable {
                            this@HorizontalShelfScreen.sendEvent(ShelfEvents.ExpandShelfEvent(index))
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.mainBackgroundColor)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Strings.show_all,
                                style = ApplicationTheme.typography.footnoteBold,
                                color = ApplicationTheme.colors.mainTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}