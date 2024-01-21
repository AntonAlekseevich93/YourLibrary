package menu_bar

import ApplicationTheme
import Drawable
import Strings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tooltip_area.TooltipIconArea
import tooltip_area.TooltipItem
import tooltip_area.TooltipPosition

@Composable
fun LeftMenuBar(
    searchListener: () -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    open: () -> Unit,
    createBookListener: () -> Unit,
    selectAnotherVaultListener: () -> Unit,
) {
    Row(modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundWindowDarkColor)) {
        Column(
            modifier = Modifier.widthIn(max = 42.dp).fillMaxHeight().width(42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TooltipIconArea(
                text = Strings.search,
                drawableResName = Drawable.drawable_ic_search,
                modifier = Modifier.padding(top = 10.dp),
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                onClick = searchListener
            )

            TooltipIconArea(
                text = Strings.add_book,
                drawableResName = Drawable.drawable_ic_add,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                createBookListener.invoke()
            }

            TooltipIconArea(
                text = Strings.quotes,
                drawableResName = Drawable.drawable_ic_quotes,
                modifier = Modifier.padding(top = 10.dp),
                iconSize = 18.dp,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                pointerInnerPadding = 4.dp
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = Strings.tags,
                drawableResName = Drawable.drawable_ic_tag,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                open.invoke()
            }

            TooltipIconArea(
                text = "Активность",//todo
                drawableResName = Drawable.drawable_ic_activity,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = "Открыть случайную цитату",//todo
                drawableResName = Drawable.drawable_ic_random,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))

            TooltipIconArea(
                text = Strings.another_storage,
                drawableResName = Drawable.drawable_ic_folder,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = selectAnotherVaultListener,
            )
            TooltipIconArea(
                text = Strings.settings,
                drawableResName = Drawable.drawable_ic_settings,
                tooltipCallback = {
                    tooltipCallback.invoke(it.apply { position = TooltipPosition.RIGHT })
                },
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                println("открыли меню")
            }
        }

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            thickness = 1.dp,
            color = ApplicationTheme.colors.divider
        )
    }
}