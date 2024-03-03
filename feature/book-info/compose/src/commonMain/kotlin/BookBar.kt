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
import main_models.TooltipPosition
import models.BookScreenEvents
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import toolbar.ToolbarEvents
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea

@Composable
fun BaseEventScope<BaseEvent>.FullBookBar(
    platform: Platform,
    isFullscreen: State<Boolean>,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: State<Boolean>,
    isEditMode: State<Boolean>,
    hideSaveButton: State<Boolean>,
    onFullscreen: () -> Unit,
) {
    when (platform) {
        is Platform.MOBILE -> {
            BookBar(
                isFullscreen = isFullscreen,
                showLeftDrawer = showLeftDrawer,
                showRightDrawer = showRightDrawer,
                isEditMode = isEditMode,
                onFullscreen = onFullscreen,
                hideSaveButton = hideSaveButton,
            )
        }

        is Platform.DESKTOP -> {
            BookBar(
                isFullscreen = isFullscreen,
                showLeftDrawer = showLeftDrawer,
                showRightDrawer = showRightDrawer,
                isEditMode = isEditMode,
                onFullscreen = onFullscreen,
                hideSaveButton = hideSaveButton,
            )
        }
    }
}

@Composable
internal fun BaseEventScope<BaseEvent>.BookBar(
    isFullscreen: State<Boolean>,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: State<Boolean>,
    hideSaveButton: State<Boolean>,
    isEditMode: State<Boolean>,
    onFullscreen: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = 50.dp, max = 50.dp)
            .background(ApplicationTheme.colors.mainToolbarColor),
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
                        this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                    onClick = {
                        this@BookBar.sendEvent(ToolbarEvents.ToMain)
                    }
                )

                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    modifier = Modifier.padding(start = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = {
                        this@BookBar.sendEvent(DrawerEvents.OpenLeftDrawerOrCloseEvent)
                    },
                    tooltipCallback = {
                        this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                )
            }
        }

        AnimatedVisibility(visible = !hideSaveButton.value) {
            TooltipIconArea(
                text = if (isEditMode.value) Strings.save else Strings.edit,
                drawableResName = if (isEditMode.value) Drawable.drawable_ic_save else Drawable.drawable_ic_edit,
                modifier = Modifier.padding(start = 10.dp),
                iconSize = 18.dp,
                iconTint = if (isEditMode.value) ApplicationTheme.colors.successColor else ApplicationTheme.colors.mainIconsColor,
                pointerInnerPadding = 4.dp,
                tooltipCallback = {
                    this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.BOTTOM
                    }))
                },
                onClick = {
                    if (isEditMode.value) {
                        this@BookBar.sendEvent(BookScreenEvents.SaveBookAfterEditing)
                    } else {
                        this@BookBar.sendEvent(BookScreenEvents.SetEditMode)
                    }
                }
            )
        }

        TooltipIconArea(
            text = Strings.tags,
            drawableResName = Drawable.drawable_ic_tag,
            modifier = Modifier.padding(start = 10.dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp,
            tooltipCallback = {
                this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
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
                this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.BOTTOM
                }))
            },
        ) {
            //todo
        }
//        TooltipIconArea(
//            text = Strings.add_calendar,
//            drawableResName = Drawable.drawable_ic_calendar,
//            modifier = Modifier.padding(start = 10.dp),
//            iconSize = 18.dp,
//            pointerInnerPadding = 4.dp,
//            tooltipCallback = {
//                tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
//            },
//        ) {
//            //todo
//        }
//        TooltipIconArea(
//            text = Strings.add_bookmark,
//            drawableResName = Drawable.drawable_ic_bookmark,
//            modifier = Modifier.padding(start = 10.dp),
//            iconSize = 18.dp,
//            pointerInnerPadding = 4.dp,
//            tooltipCallback = {
//                tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
//            },
//        ) {
//            //todo
//        }
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
                    onClick = {
                        this@BookBar.sendEvent(DrawerEvents.OpenRightDrawerOrCloseEvent)
                    },
                    tooltipCallback = {
                        this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM_LEFT
                        }))
                    },
                )
                Divider(
                    modifier = Modifier.width(1.dp).height(24.dp),
                    color = ApplicationTheme.colors.searchDividerColor
                )
                if (!showLeftDrawer.value && isFullscreen.value) {
                    TooltipIconArea(
                        text = Strings.collapse,
                        drawableResName = Drawable.drawable_ic_collapse,
                        modifier = Modifier.padding(start = 10.dp),
                        iconSize = 18.dp,
                        pointerInnerPadding = 4.dp,
                        onClick = onFullscreen,
                        tooltipCallback = {
                            this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                                position = TooltipPosition.BOTTOM_LEFT
                            }))
                        },
                    )
                } else if (!showLeftDrawer.value) {
                    TooltipIconArea(
                        text = Strings.expand,
                        drawableResName = Drawable.drawable_ic_expand,
                        modifier = Modifier.padding(start = 10.dp),
                        iconSize = 18.dp,
                        pointerInnerPadding = 4.dp,
                        onClick = onFullscreen,
                        tooltipCallback = {
                            this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                                position = TooltipPosition.BOTTOM_LEFT
                            }))
                        },
                    )
                }
                TooltipIconArea(
                    text = Strings.close,
                    drawableResName = Drawable.drawable_ic_close,
                    modifier = Modifier.padding(start = 10.dp, end = 16.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = {
                        this@BookBar.sendEvent(BookScreenEvents.BookScreenCloseEvent)
                    },
                    tooltipCallback = {
                        this@BookBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM_LEFT
                        }))
                    },
                )
            }
        }
    }
}