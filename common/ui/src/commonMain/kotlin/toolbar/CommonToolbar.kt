package toolbar

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.TooltipPosition
import navigation_drawer.contents.models.DrawerEvents
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea

@Composable
fun BaseEventScope<BaseEvent>.CommonToolbar(
    showLeftDrawer: State<Boolean>,
    content: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp, max = 50.dp)
            .background(ApplicationTheme.colors.mainToolbarColor),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = !showLeftDrawer.value,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut(spring(stiffness = Spring.StiffnessHigh))
        ) {
            Row {
                TooltipIconArea(
                    text = Strings.to_main,
                    drawableResName = Drawable.drawable_ic_home,
                    modifier = Modifier.padding(start = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    tooltipCallback = {
                        this@CommonToolbar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                    onClick = {
                        this@CommonToolbar.sendEvent(ToolbarEvents.ToMain)
                    }
                )

                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    modifier = Modifier.padding(start = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = {
                        this@CommonToolbar.sendEvent(DrawerEvents.OpenLeftDrawerOrCloseEvent)
                    },
                    tooltipCallback = {
                        this@CommonToolbar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                )
            }
        }

        content?.invoke()
        Spacer(Modifier.weight(1f, fill = true))

        TooltipIconArea(
            text = Strings.close,
            drawableResName = Drawable.drawable_ic_close,
            modifier = Modifier.padding(start = 10.dp, end = 16.dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp,
            onClick = {
                this@CommonToolbar.sendEvent(ToolbarEvents.OnCloseEvent)
            },
            tooltipCallback = {
                this@CommonToolbar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM_LEFT
                }))
            },
        )
    }
}