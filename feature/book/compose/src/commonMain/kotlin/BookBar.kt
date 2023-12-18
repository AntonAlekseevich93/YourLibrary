import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import platform.Platform
import tooltip_area.TooltipIconArea
import tooltip_area.TooltipItem
import tooltip_area.TooltipPosition

@Composable
fun FullBookBar(
    platform: Platform,
    isFullscreen: State<Boolean>,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: State<Boolean>,
    onFullscreen: () -> Unit,
    openLeftDrawerListener: () -> Unit,
    openRightDrawerListener: () -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    onClose: () -> Unit,
) {
    when (platform) {
        Platform.MOBILE -> {

        }

        Platform.DESKTOP -> {
            BookBar(
                isFullscreen = isFullscreen,
                showLeftDrawer = showLeftDrawer,
                showRightDrawer = showRightDrawer,
                onFullscreen = onFullscreen,
                openLeftDrawerListener = openLeftDrawerListener,
                openRightDrawerListener = openRightDrawerListener,
                onClose = onClose,
                tooltipCallback = tooltipCallback
            )
        }
    }
}

@Composable
internal fun BookBar(
    isFullscreen: State<Boolean>,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: State<Boolean>,
    onFullscreen: () -> Unit,
    openLeftDrawerListener: () -> Unit,
    openRightDrawerListener: () -> Unit,
    onClose: () -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp, max = 50.dp).padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = isFullscreen.value && !showLeftDrawer.value,
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
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                ) {
                    //todo
                }

                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    modifier = Modifier.padding(start = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = openLeftDrawerListener,
                    tooltipCallback = {
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                )
            }
        }

        TooltipIconArea(
            text = Strings.tags,
            drawableResName = Drawable.drawable_ic_tag,
            modifier = Modifier.padding(start = 10.dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp,
            tooltipCallback = {
                tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
            },
        ) {
            //todo
        }
        TooltipIconArea(
            text = Strings.add_subtask,
            drawableResName = Drawable.drawable_ic_subtask,
            modifier = Modifier.padding(start = 10.dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp,
            tooltipCallback = {
                tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
            },
        ) {
            //todo
        }
        TooltipIconArea(
            text = Strings.add_calendar,
            drawableResName = Drawable.drawable_ic_calendar,
            modifier = Modifier.padding(start = 10.dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp,
            tooltipCallback = {
                tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
            },
        ) {
            //todo
        }
        TooltipIconArea(
            text = Strings.add_bookmark,
            drawableResName = Drawable.drawable_ic_bookmark,
            modifier = Modifier.padding(start = 10.dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp,
            tooltipCallback = {
                tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
            },
        ) {
            //todo
        }
        Spacer(modifier = Modifier.weight(1f, fill = true))
        AnimatedVisibility(
            visible = !showRightDrawer.value,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
            exit = fadeOut(spring(stiffness = Spring.StiffnessHigh))
        ) {
            Row {
                TooltipIconArea(
                    text = Strings.sidebar,
                    drawableResName = Drawable.drawable_ic_note_sidebar,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = openRightDrawerListener,
                    tooltipCallback = {
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                )
                Divider(
                    modifier = Modifier.width(1.dp).height(24.dp),
                    color = ApplicationTheme.colors.searchDividerColor
                )
                if (isFullscreen.value) {
                    TooltipIconArea(
                        text = Strings.collapse,
                        drawableResName = Drawable.drawable_ic_collapse,
                        modifier = Modifier.padding(start = 10.dp),
                        iconSize = 18.dp,
                        pointerInnerPadding = 4.dp,
                        onClick = onFullscreen,
                        tooltipCallback = {
                            tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                        },
                    )
                } else {
                    TooltipIconArea(
                        text = Strings.expand,
                        drawableResName = Drawable.drawable_ic_expand,
                        modifier = Modifier.padding(start = 10.dp),
                        iconSize = 18.dp,
                        pointerInnerPadding = 4.dp,
                        onClick = onFullscreen,
                        tooltipCallback = {
                            tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                        },
                    )
                }
                TooltipIconArea(
                    text = Strings.close,
                    drawableResName = Drawable.drawable_ic_close,
                    modifier = Modifier.padding(start = 10.dp, end = 16.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = onClose,
                    tooltipCallback = {
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                )
            }
        }
    }
}