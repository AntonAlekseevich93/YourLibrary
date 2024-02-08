package navigation_drawer

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.TooltipPosition
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea


@Composable
fun BaseEventScope<BaseEvent>.PlatformRightDrawerContent(
    platform: Platform,
    isCanClose: Boolean,
    isFullscreen: State<Boolean>,
    selectedItem: SelectedRightDrawerItem = SelectedRightDrawerItem.NONE,
    closeWindow: () -> Unit,
    expandOrCollapseListener: () -> Unit,
) {
    when (platform) {
        Platform.DESKTOP -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainDrawerBackground,
            ) {
                RightDrawerContent(
                    isCanClose = isCanClose,
                    isFullscreen = isFullscreen,
                    selectedItem = selectedItem,
                    expandOrCollapseListener = expandOrCollapseListener,
                    closeWindow = closeWindow,
                )
            }
        }

        else -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainDrawerBackground,
            ) {
                RightDrawerContent(
                    isCanClose = isCanClose,
                    isFullscreen = isFullscreen,
                    selectedItem = selectedItem,
                    expandOrCollapseListener = expandOrCollapseListener,
                    closeWindow = closeWindow,
                )
            }
        }
    }
}

@Composable
fun BaseEventScope<BaseEvent>.RightDrawerContent(
    isCanClose: Boolean,
    isFullscreen: State<Boolean>,
    selectedItem: SelectedRightDrawerItem,
    closeWindow: () -> Unit,
    expandOrCollapseListener: () -> Unit,
) {

    Box(modifier = Modifier.widthIn(max = 300.dp)) {
        Column {
            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 14.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TooltipIconArea(
                    text = Strings.structure,
                    drawableResName = Drawable.drawable_ic_structure,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    pointerIsActiveCardColor =
                    if (selectedItem == SelectedRightDrawerItem.STRUCTURE)
                        ApplicationTheme.colors.pointerIsActiveCardColorDark
                    else
                        ApplicationTheme.colors.pointerIsActiveCardColor,
                    onClick = {
                        this@RightDrawerContent.sendEvent(DrawerEvents.OpenRightDrawerOrCloseEvent)
                    },
                    iconTint = ApplicationTheme.colors.searchIconColor,
                    tooltipCallback = {
                        this@RightDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                )

                Spacer(modifier = Modifier.weight(1f, fill = true))

                TooltipIconArea(
                    text = Strings.sidebar,
                    drawableResName = Drawable.drawable_ic_note_sidebar,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = {
                        this@RightDrawerContent.sendEvent(DrawerEvents.OpenRightDrawerOrCloseEvent)
                    },
                    tooltipCallback = {
                        this@RightDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                            position = TooltipPosition.BOTTOM
                        }))
                    },
                )
                if (isCanClose) {
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
                            onClick = expandOrCollapseListener,
                            tooltipCallback = {
                                this@RightDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                                    position = TooltipPosition.BOTTOM
                                }))
                            },
                        )
                    } else {
                        TooltipIconArea(
                            text = Strings.expand,
                            drawableResName = Drawable.drawable_ic_expand,
                            modifier = Modifier.padding(start = 10.dp),
                            iconSize = 18.dp,
                            pointerInnerPadding = 4.dp,
                            onClick = expandOrCollapseListener,
                            tooltipCallback = {
                                this@RightDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                                    position = TooltipPosition.BOTTOM
                                }))
                            },
                        )
                    }
                    TooltipIconArea(
                        text = Strings.close,
                        drawableResName = Drawable.drawable_ic_close,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        iconSize = 18.dp,
                        pointerInnerPadding = 4.dp,
                        onClick = closeWindow,
                        tooltipCallback = {
                            this@RightDrawerContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                                position = TooltipPosition.BOTTOM
                            }))
                        },
                    )
                }
            }
            Divider(
                modifier = Modifier.fillMaxWidth().height(1.dp),
                color = ApplicationTheme.colors.divider
            )
        }
    }
}

enum class SelectedRightDrawerItem {
    NONE,
    STRUCTURE,
}
