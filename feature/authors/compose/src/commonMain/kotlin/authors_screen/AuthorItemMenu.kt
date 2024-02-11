package authors_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import main_models.AuthorVo
import main_models.TooltipPosition
import models.AuthorsEvents
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea

@Composable
fun BaseEventScope<BaseEvent>.AuthorItemMenu(authorVo: AuthorVo) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 0.dp, bottom = 3.dp, start = 16.dp, end = 16.dp)
    ) {
        TooltipIconArea(
            text = Strings.all_books,
            iconSize = 16.dp,
            iconTint = ApplicationTheme.colors.mainIconsColor,
            drawableResName = Drawable.drawable_ic_book,
            tooltipCallback = {
                this@AuthorItemMenu.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
            },
            modifier = Modifier.padding(end = 6.dp),
            onClick = {

            }
        )

        TooltipIconArea(
            text = Strings.quotes,
            iconSize = 16.dp,
            iconTint = ApplicationTheme.colors.mainIconsColor,
            drawableResName = Drawable.drawable_ic_quotes,
            tooltipCallback = {
                this@AuthorItemMenu.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
            },
            modifier = Modifier.padding(end = 6.dp),
            onClick = {

            }
        )

        TooltipIconArea(
            text = Strings.join_to_author,
            iconSize = 16.dp,
            imageModifier = Modifier.rotate(90f),
            iconTint = ApplicationTheme.colors.mainIconsColor,
            drawableResName = Drawable.drawable_ic_join,
            tooltipCallback = {
                this@AuthorItemMenu.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
            },
            modifier = Modifier.padding(end = 6.dp),
            onClick = {
                this@AuthorItemMenu.sendEvent(AuthorsEvents.OpenJoinAuthorsScreen(authorVo))
            }
        )

        TooltipIconArea(
            text = Strings.rename,
            iconSize = 16.dp,
            iconTint = ApplicationTheme.colors.mainIconsColor,
            drawableResName = Drawable.drawable_ic_rename,
            tooltipCallback = {
                this@AuthorItemMenu.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
            },
            modifier = Modifier.padding(end = 6.dp),
            onClick = {

            }
        )

        TooltipIconArea(
            text = Strings.delete,
            iconSize = 16.dp,
            iconTint = ApplicationTheme.colors.mainIconsColor,
            drawableResName = Drawable.drawable_ic_trash,
            tooltipCallback = {
                this@AuthorItemMenu.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
            },
            modifier = Modifier.padding(end = 6.dp),
            onClick = {

            }
        )
    }
}