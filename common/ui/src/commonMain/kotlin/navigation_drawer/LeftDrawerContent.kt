package navigation_drawer

import ApplicationTheme
import Drawable
import Strings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import platform.Platform
import tooltip_area.TooltipIconArea
import tooltip_area.TooltipItem
import tooltip_area.TooltipPosition


@Composable
fun PlatformLeftDrawerContent(
    platform: Platform,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit)? = null,
    closeSidebarListener: () -> Unit
) {
    when (platform) {
        Platform.DESKTOP -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
            ) {
                LeftDrawerContent(closeSidebarListener,tooltipCallback)
            }
        }


        else -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
            ) {
                LeftDrawerContent(closeSidebarListener,tooltipCallback)
            }
        }
    }
}

@Composable
fun LeftDrawerContent(
    closeSidebarListener: () -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit)? = null,
) {
    Box(modifier = Modifier.widthIn(max = 300.dp)) {
        Column {
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Проекты", //todo текст передается
                    modifier = Modifier.padding(start = 32.dp),
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f, fill = true))

                TooltipIconArea(
                    text = Strings.to_main,
                    drawableResName = Drawable.drawable_ic_home,
                    tooltipCallback = {
                        tooltipCallback?.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp),

                    ) {
                    closeSidebarListener.invoke()
                }
                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    tooltipCallback = {
                        tooltipCallback?.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                    modifier = Modifier.padding(end = 12.dp),

                    ) {
                    closeSidebarListener.invoke()
                }
            }
        }
    }
}
