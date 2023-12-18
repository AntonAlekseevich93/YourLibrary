package sub_app_bar

import ApplicationTheme
import Drawable
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import tooltip_area.TooltipIconArea
import tooltip_area.TooltipItem
import tooltip_area.TooltipPosition

@Composable
fun SubAppBar(
    projectName: String,
    isOpenedType: ViewsType,
    modifier: Modifier = Modifier,
    isOpenedSidebar: State<Boolean>,
    openSidebarListener: () -> Unit,
    selectedViewsTypes: SnapshotStateList<ViewsType>,
    isCheckedTypes: SnapshotStateList<ViewsType>,
    openViewType: (type: ViewsType) -> Unit,
    switchViewTypesListener: (isChecked: Boolean, type: ViewsType) -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    closeViewsTypeDropdown: () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = !isOpenedSidebar.value) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TooltipIconArea(
                    text = Strings.to_main,
                    drawableResName = Drawable.drawable_ic_home,
                    modifier = Modifier.padding(end = 6.dp),
                    pointerEventBackgroundDisable = ApplicationTheme.colors.mainBackgroundColor,
                    tooltipCallback = {
                        tooltipCallback?.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                ) {
                }
                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    modifier = Modifier.padding(end = 12.dp),
                    pointerEventBackgroundDisable = ApplicationTheme.colors.mainBackgroundColor,
                    imageModifier = Modifier.rotate(180f),
                    tooltipCallback = {
                        tooltipCallback?.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    },
                ) {
                    openSidebarListener.invoke()
                }
            }
        }
        Text(
            text = projectName,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ViewTypesBlock(
            isOpenedType = isOpenedType,
            openViewType = openViewType,
            selectedViewsTypes = selectedViewsTypes,
            isCheckedTypes = isCheckedTypes,
            switchChangedListener = switchViewTypesListener,
            closeViewsTypeDropdown = closeViewsTypeDropdown,
            tooltipCallback = tooltipCallback,
        )
    }
}

@Composable
fun ViewTypesBlock(
    isOpenedType: ViewsType,
    selectedViewsTypes: SnapshotStateList<ViewsType>,
    isCheckedTypes: SnapshotStateList<ViewsType>,
    openViewType: (type: ViewsType) -> Unit,
    switchChangedListener: (isChecked: Boolean, type: ViewsType) -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    closeViewsTypeDropdown: () -> Unit,
) {
    val selectedType = remember(isOpenedType) { mutableStateOf(isOpenedType) }
    val isExpandedDropDownViewsMenu = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(6.dp),
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundDark)
    ) {//todo переделать токен цвет) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            AnimatedVisibility(visible = selectedViewsTypes.contains(ViewsType.LIST)) {
                TooltipIconArea(
                    text = Strings.list,
                    drawableResName = Drawable.drawable_ic_list,
                    isSelected = selectedType.value == ViewsType.LIST,
                    iconSize = 16.dp,
                    onClick = {
                        selectedType.value = ViewsType.LIST
                        openViewType.invoke(ViewsType.LIST)
                    },
                    tooltipCallback = {
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    }
                )
            }
            AnimatedVisibility(visible = selectedViewsTypes.contains(ViewsType.KANBAN)) {
                TooltipIconArea(
                    text = Strings.kanban,
                    drawableResName = Drawable.drawable_ic_kanban,
                    isSelected = selectedType.value == ViewsType.KANBAN,
                    iconSize = 16.dp,
                    onClick = {
                        selectedType.value = ViewsType.KANBAN
                        openViewType.invoke(ViewsType.KANBAN)
                    },
                    tooltipCallback = {
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    }
                )
            }
            AnimatedVisibility(visible = selectedViewsTypes.contains(ViewsType.CALENDAR)) {
                TooltipIconArea(
                    text = Strings.calendar,
                    drawableResName = Drawable.drawable_ic_calendar,
                    isSelected = selectedType.value == ViewsType.CALENDAR,
                    iconSize = 16.dp,
                    onClick = {
                        selectedType.value = ViewsType.CALENDAR
                        openViewType.invoke(ViewsType.CALENDAR)
                    },
                    tooltipCallback = {
                        tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                    }
                )
            }
            Box {
                Box(
                    modifier = Modifier.defaultMinSize(minHeight = 26.dp).padding(start = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    TooltipIconArea(
                        text = Strings.edit,
                        drawableResName = Drawable.drawable_ic_plus,
                        iconSize = 8.dp,
                        onClick = {
                            isExpandedDropDownViewsMenu.value = true
                        },
                        tooltipCallback = {
                            tooltipCallback.invoke(it.apply { position = TooltipPosition.BOTTOM })
                        }
                    )
                }
                DropDownMenuViews(
                    isExpanded = isExpandedDropDownViewsMenu.value,
                    onDismissRequest = {
                        isExpandedDropDownViewsMenu.value = false
                        closeViewsTypeDropdown.invoke()
                    },
                    isCheckedTypes = isCheckedTypes,
                    switchChangedListener = switchChangedListener
                )
            }
        }
    }
}

@Composable
internal fun DropDownMenuViews(
    isExpanded: Boolean,
    onDismissRequest: (isExpanded: Boolean) -> Unit,
    isCheckedTypes: SnapshotStateList<ViewsType>,
    switchChangedListener: (isChecked: Boolean, type: ViewsType) -> Unit,
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(RoundedCornerShape(10.dp)),
        colors = MaterialTheme.colors.copy(
            surface = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
        ),
    ) {
        DropdownMenu(
            modifier = Modifier,
            expanded = isExpanded,
            onDismissRequest = { onDismissRequest.invoke(!isExpanded) }
        ) {
            DropDownItemView(
                text = Strings.list,
                iconResName = Drawable.drawable_ic_list,
                isChecked = isCheckedTypes.contains(ViewsType.LIST),
                switchChangedListener = { isChecked ->
                    if (!isChecked && isCheckedTypes.size == 1) {
                        //todo показать предупреждение что нельзя убрать последний
                    } else {
                        switchChangedListener.invoke(isChecked, ViewsType.LIST)
                    }
                }
            )
            DropDownItemView(
                text = Strings.kanban,
                iconResName = Drawable.drawable_ic_kanban,
                isChecked = isCheckedTypes.contains(ViewsType.KANBAN),
                switchChangedListener = { isChecked ->
                    if (!isChecked && isCheckedTypes.size == 1) {
                        //todo показать предупреждение что нельзя убрать последний
                    } else {
                        switchChangedListener.invoke(isChecked, ViewsType.KANBAN)
                    }
                }
            )
            DropDownItemView(
                text = Strings.calendar,
                iconResName = Drawable.drawable_ic_calendar,
                isChecked = isCheckedTypes.contains(ViewsType.CALENDAR),
                switchChangedListener = { isChecked ->
                    if (!isChecked && isCheckedTypes.size == 1) {
                        //todo показать предупреждение что нельзя убрать последний
                    } else {
                        switchChangedListener.invoke(isChecked, ViewsType.CALENDAR)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun DropDownItemView(
    text: String,
    iconResName: String,
    isChecked: Boolean,
    switchChangedListener: (isChecked: Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(iconResName),
            contentDescription = null,
            colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
            modifier = Modifier
                .padding(start = 16.dp, end = 10.dp)
                .size(16.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(end = 50.dp),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor
        )
        Spacer(modifier = Modifier.weight(1f, fill = true))
        Switch(
            modifier = Modifier.scale(0.7f),
            checked = isChecked,
            onCheckedChange = { switchChangedListener.invoke(!isChecked) },
            colors = SwitchDefaults.colors(
                uncheckedBorderColor = Color.Transparent,
                uncheckedThumbColor = Color(0xFF3a3a3d), //todo token
                uncheckedTrackColor = Color(0xFF878787),//todo token
                checkedBorderColor = Color.Transparent,
                checkedThumbColor = Color(0xFF3a3a3d),//todo token
                checkedTrackColor = Color(0xFFfeab7d)//todo token
            )
        )
    }
}

enum class ViewsType {
    KANBAN,
    LIST,
    CALENDAR
}