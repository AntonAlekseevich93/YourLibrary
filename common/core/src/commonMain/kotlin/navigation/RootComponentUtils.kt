package navigation

import com.arkivanov.decompose.router.stack.active

fun RootComponent.activeScreenAsBookInfoOrNull() =
    this.screenStack.active.instance as? RootComponent.Screen.BookInfoScreen

fun RootComponent.activeScreenAsMainOrNull() =
    this.screenStack.active.instance as? RootComponent.Screen.MainScreen

fun RootComponent.isBookInfo() =
    this.screenStack.active.instance as? RootComponent.Screen.BookInfoScreen != null

fun RootComponent.isMainScreen() =
    this.screenStack.active.instance as? RootComponent.Screen.MainScreen != null

fun RootComponent.isBookCreatorScreen() =
    this.screenStack.active.instance as? RootComponent.Screen.BookCreatorScreen != null

fun RootComponent.isProfileScreen() =
    this.screenStack.active.instance as? RootComponent.Screen.ProfileScreen != null

fun RootComponent.isSettingsScreen() =
    this.screenStack.active.instance as? RootComponent.Screen.SettingsScreen != null