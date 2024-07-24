package bottom_app_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

val bottomBarTabs = listOf(
    BottomBarTab.Home,
    BottomBarTab.Creator,
    BottomBarTab.Profile,
    BottomBarTab.Settings,
)

sealed class BottomBarTab(val title: String, val icon: ImageVector, val color: Color) {
    data object Home : BottomBarTab(
        title = "Главная",
        icon = Icons.Rounded.Home,
        color = Color(0xFFd9d9d9)
    )

    data object Creator : BottomBarTab(
        title = "Добавить",
        icon = Icons.Rounded.Add,
        color = Color(0xFFd9d9d9)
    )

    data object Profile : BottomBarTab(
        title = "Профиль",
        icon = Icons.Rounded.Person,
        color = Color(0xFFd9d9d9)
    )

    data object Settings : BottomBarTab(
        title = "Настройки",
        icon = Icons.Rounded.Settings,
        color = Color(0xFFd9d9d9)
    )
}