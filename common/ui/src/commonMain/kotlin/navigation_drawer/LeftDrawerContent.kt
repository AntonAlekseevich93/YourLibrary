package navigation_drawer

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import main_models.TooltipPosition
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_home
import yourlibrary.common.resources.generated.resources.ic_sidebar

@Composable
fun BaseEventScope<BaseEvent>.PlatformLeftDrawerContent(
    title: String,
    platform: Platform,
    canShowHomeButton: State<Boolean>,
    content: @Composable () -> Unit,
) {
    when (platform) {
        is Platform.DESKTOP -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainDrawerBackground,
            ) {
                LeftDrawerContent(title, canShowHomeButton, content)
            }
        }


        else -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainDrawerBackground,
            ) {
                LeftDrawerContent(title, canShowHomeButton, content)
            }
        }
    }
}

@Composable
fun BaseEventScope<BaseEvent>.LeftDrawerContent(
    title: String,
    canShowHomeButton: State<Boolean>,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.widthIn(max = 400.dp)) {
        Column {
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 16.dp, end = 6.dp),
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f, fill = true))

                AnimatedVisibility(
                    canShowHomeButton.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    TooltipIconArea(
                        text = Strings.to_main,
                        drawableResName = Res.drawable.ic_home,
                        tooltipCallback = {
                            this@LeftDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                                position = TooltipPosition.BOTTOM
                            }))
                        },
                        modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                        onClick = {
                            this@LeftDrawerContent.sendEvent(DrawerEvents.ToMain)
                        }
                    )
                }
                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Res.drawable.ic_sidebar,
                    tooltipCallback = {
                        this@LeftDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                    modifier = Modifier.padding(end = 12.dp),
                    onClick = {
                        this@LeftDrawerContent.sendEvent(DrawerEvents.OpenLeftDrawerOrCloseEvent)
                    }
                )
            }
            Divider(
                modifier = Modifier.padding(top = 6.dp).fillMaxWidth().height(1.dp),
                thickness = 1.dp,
                color = ApplicationTheme.colors.divider
            )
            content.invoke()
        }
    }
}
