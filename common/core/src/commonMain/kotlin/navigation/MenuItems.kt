package navigation

sealed class MenuItem {
    data object MAIN : MenuItem()
    data object CREATOR : MenuItem()
    data object PROFILE : MenuItem()
    data object SETTINGS : MenuItem()
}