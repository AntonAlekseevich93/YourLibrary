package menu_bar

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import main_models.TooltipPosition
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_activity
import yourlibrary.common.resources.generated.resources.ic_add
import yourlibrary.common.resources.generated.resources.ic_authors
import yourlibrary.common.resources.generated.resources.ic_folder
import yourlibrary.common.resources.generated.resources.ic_quote
import yourlibrary.common.resources.generated.resources.ic_random
import yourlibrary.common.resources.generated.resources.ic_search
import yourlibrary.common.resources.generated.resources.ic_server
import yourlibrary.common.resources.generated.resources.ic_settings
import yourlibrary.common.resources.generated.resources.ic_tag
import yourlibrary.common.resources.generated.resources.ic_user

@Composable
fun BaseEventScope<BaseEvent>.LeftMenuBar(
    open: () -> Unit,
) {
    Row(modifier = Modifier.background(ApplicationTheme.colors.mainLeftBarColor)) {
        Column(
            modifier = Modifier.widthIn(max = 42.dp).fillMaxHeight().width(42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TooltipIconArea(
                text = Strings.search,
                drawableResName = Res.drawable.ic_search,
                modifier = Modifier.padding(top = 10.dp),
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnSearchClickEvent)
                }
            )

            TooltipIconArea(
                text = Strings.add_book,
                drawableResName = Res.drawable.ic_add,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnCreateBookClickEvent)
                }
            )

            TooltipIconArea(
                text = Strings.quotes,
                drawableResName = Res.drawable.ic_quote,
                modifier = Modifier.padding(top = 10.dp),
                iconSize = 18.dp,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                pointerInnerPadding = 4.dp
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = Strings.authors,
                drawableResName = Res.drawable.ic_authors,
                modifier = Modifier.padding(top = 10.dp),
                iconSize = 18.dp,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                pointerInnerPadding = 4.dp,
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnAuthorsClickEvent)
                }
            )

            TooltipIconArea(
                text = Strings.tags,
                drawableResName = Res.drawable.ic_tag,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                open.invoke()
            }

            TooltipIconArea(
                text = "Активность",//todo
                drawableResName = Res.drawable.ic_activity,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = "Открыть случайную цитату",//todo
                drawableResName = Res.drawable.ic_random,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))

            TooltipIconArea(
                text = Strings.admin_panel,
                drawableResName = Res.drawable.ic_server,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnAdminPanelClickEvent)
                },
            )

            TooltipIconArea(
                text = Strings.profile,
                drawableResName = Res.drawable.ic_user,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnProfileClickEvent)
                },
            )

            TooltipIconArea(
                text = Strings.another_storage,
                drawableResName = Res.drawable.ic_folder,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnSelectAnotherVaultEvent)
                },
            )
            TooltipIconArea(
                text = Strings.settings,
                drawableResName = Res.drawable.ic_settings,
                tooltipCallback = {
                    this@LeftMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.RIGHT
                    }))
                },
                modifier = Modifier.padding(bottom = 10.dp),
                onClick = {
                    this@LeftMenuBar.sendEvent(LeftMenuBarEvents.OnSettingsClickEvent)
                }
            )
        }

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            thickness = 1.dp,
            color = ApplicationTheme.colors.divider
        )
    }
}

@Composable
fun BaseEventScope<BaseEvent>.BottomMenuBar(
    open: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().fillMaxHeight().background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TooltipIconArea(
            text = Strings.add_book,
            drawableResName = Res.drawable.ic_add,
            tooltipCallback = {
                this@BottomMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.RIGHT
                }))
            },
            modifier = Modifier,
            onClick = {
                this@BottomMenuBar.sendEvent(LeftMenuBarEvents.OnCreateBookClickEvent)
            }
        )
        Spacer(Modifier.weight(1f))

        TooltipIconArea(
            text = Strings.admin_panel,
            drawableResName = Res.drawable.ic_server,
            tooltipCallback = {
                this@BottomMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.RIGHT
                }))
            },
            modifier = Modifier,
            onClick = {
                this@BottomMenuBar.sendEvent(LeftMenuBarEvents.OnAdminPanelClickEvent)
            },
        )
        Spacer(Modifier.weight(1f))
        TooltipIconArea(
            text = Strings.settings,
            drawableResName = Res.drawable.ic_settings,
            tooltipCallback = {
                this@BottomMenuBar.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                    position = TooltipPosition.RIGHT
                }))
            },
            modifier = Modifier,
            onClick = {
                this@BottomMenuBar.sendEvent(LeftMenuBarEvents.OnSettingsClickEvent)
            }
        )
    }
}