package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable
import navigation.screens.BookCreatorScreenComponent
import navigation.screens.BookInfoComponent
import navigation.screens.BooksListInfoScreenComponent
import navigation.screens.DefaultBookCreatorScreenComponent
import navigation.screens.DefaultBookInfoComponent
import navigation.screens.DefaultBooksListInfoScreenComponent
import navigation.screens.DefaultMainScreenComponent
import navigation.screens.DefaultModerationScreenComponent
import navigation.screens.DefaultProfileScreenComponent
import navigation.screens.DefaultSettingsScreenComponent
import navigation.screens.MainScreenComponent
import navigation.screens.ModerationScreenComponent
import navigation.screens.ProfileScreenComponent
import navigation.screens.SettingsScreenComponent

interface RootComponent : BackHandlerOwner {

    val screenStack: Value<ChildStack<*, Screen>>
    val screensStackKeys: MutableList<Int>
    val getNextStackKey: Int

    fun onBackClicked()

    fun menuClick(menuItem: MenuItem)

    sealed class Screen {
        class MainScreen(val component: MainScreenComponent) : Screen()
        class BookInfoScreen(val component: BookInfoComponent) : Screen()
        class BookCreatorScreen(val component: BookCreatorScreenComponent) : Screen()
        class ProfileScreen(val component: ProfileScreenComponent) : Screen()
        class SettingsScreen(val component: SettingsScreenComponent) : Screen()
        class BooksListInfoScreen(val component: BooksListInfoScreenComponent) : Screen()
        class ModerationScreen(val component: ModerationScreenComponent) : Screen()
    }
}


class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext, BackHandlerOwner {
    override val screensStackKeys: MutableList<Int> = mutableListOf(1)
    override val getNextStackKey: Int
        get() = (screensStackKeys.lastOrNull() ?: 1) + 1
    private val getCurrentStackKey: Int
        get() = screensStackKeys.lastOrNull() ?: 1
    private val isDefaultScreenOpen //todo нужно фиксить и сделать по другому
        get() = screensStackKeys.lastOrNull() ?: 1 == DEFAULT_SCREEN_ID

    private val navigation = StackNavigation<Config>()
    private var bookInfoFirstScreenId: Int = DEFAULT_SCREEN_ID

    override fun onBackClicked() {
        pop()
    }

    override fun menuClick(menuItem: MenuItem) {
        when (menuItem) {
            MenuItem.MAIN -> {
                replaceAll(Config.MainScreenConfig(DEFAULT_SCREEN_ID), DEFAULT_SCREEN_ID)
            }

            MenuItem.CREATOR -> {
                replaceAll(Config.BookCreatorConfig(DEFAULT_SCREEN_ID), DEFAULT_SCREEN_ID)
            }

            MenuItem.PROFILE -> {
                replaceAll(Config.ProfileConfig(DEFAULT_SCREEN_ID), DEFAULT_SCREEN_ID)
            }

            MenuItem.SETTINGS -> {
                replaceAll(Config.SettingsConfig(DEFAULT_SCREEN_ID), DEFAULT_SCREEN_ID)
            }
        }
    }

    override val screenStack: Value<ChildStack<*, RootComponent.Screen>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.MainScreenConfig(DEFAULT_SCREEN_ID),
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Screen =
        when (config) {
            is Config.MainScreenConfig -> RootComponent.Screen.MainScreen(
                itemMainScreen(
                    componentContext
                )
            )

            is Config.BookInfoConfig -> RootComponent.Screen.BookInfoScreen(
                itemBookInfo(
                    componentContext,
                    config
                )
            )

            is Config.BookCreatorConfig -> RootComponent.Screen.BookCreatorScreen(
                itemBookCreatorScreen(componentContext)
            )

            is Config.ProfileConfig -> RootComponent.Screen.ProfileScreen(
                itemProfileScreen(componentContext)
            )

            is Config.SettingsConfig -> RootComponent.Screen.SettingsScreen(
                itemSettingsScreen(componentContext)
            )

            is Config.BooksListInfoConfig -> RootComponent.Screen.BooksListInfoScreen(
                itemBooksListInfoScreen(componentContext)
            )

            is Config.ModerationConfig -> RootComponent.Screen.ModerationScreen(
                itemModerationScreen(componentContext)
            )
        }

    private fun itemMainScreen(componentContext: ComponentContext): MainScreenComponent =
        DefaultMainScreenComponent(
            componentContext = componentContext,
            showBookInfo = { bookId, needSaveScreenId ->
                if (needSaveScreenId) {
                    bookInfoFirstScreenId = getCurrentStackKey
                }
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(id, bookId)
                )
            },
        )

    private fun itemBookInfo(
        componentContext: ComponentContext,
        config: Config.BookInfoConfig
    ): BookInfoComponent =
        DefaultBookInfoComponent(
            componentContext = componentContext,
            onBack = { pop() },
            showBookInfo = { bookId ->
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(id, bookId)
                )
            },
            bookId = config.bookId,
            onCloseScreen = {
                popUntilStackIdFindOrFirstScreen(bookInfoFirstScreenId)
                bookInfoFirstScreenId = DEFAULT_SCREEN_ID
            }
        )

    private fun itemBookCreatorScreen(componentContext: ComponentContext): BookCreatorScreenComponent =
        DefaultBookCreatorScreenComponent(
            componentContext = componentContext,
            showBookInfo = { bookId, shortBook, needSaveScreenId ->
                if (needSaveScreenId) {
                    bookInfoFirstScreenId = getCurrentStackKey
                }
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(id, bookId)
                )
            }
        )

    private fun itemProfileScreen(componentContext: ComponentContext): ProfileScreenComponent =
        DefaultProfileScreenComponent(
            componentContext = componentContext,
        )

    private fun itemSettingsScreen(componentContext: ComponentContext): SettingsScreenComponent =
        DefaultSettingsScreenComponent(
            componentContext = componentContext,
            openModerationScreenCallback = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.ModerationConfig(id)
                )
            }
        )

    private fun itemBooksListInfoScreen(componentContext: ComponentContext): BooksListInfoScreenComponent =
        DefaultBooksListInfoScreenComponent(
            componentContext = componentContext,
        )

    private fun itemModerationScreen(componentContext: ComponentContext): ModerationScreenComponent =
        DefaultModerationScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            }
        )

    private fun popUntilStackIdFindOrFirstScreen(id: Int) {
        clearScreenStackUntilIdOrDefault(id)
        navigation.popWhile {
            it.id != id
        }
    }

    private fun pop() {
        screensStackKeys.removeLastOrNull()
        clearSavedScreensIdsIfNeed()
        navigation.pop()
    }

    private fun push(
        id: Int,
        config: Config
    ) {
        screensStackKeys.add(id)
        navigation.push(config)
    }

    private fun replaceAll(config: Config, id: Int) {
        screensStackKeys.clear()
        screensStackKeys.add(id)
        navigation.replaceAll(config)
    }

    private fun clearSavedScreensIdsIfNeed() {
        if (isDefaultScreenOpen && bookInfoFirstScreenId != DEFAULT_SCREEN_ID) {
            bookInfoFirstScreenId = DEFAULT_SCREEN_ID
        }
    }

    private fun clearScreenStackUntilIdOrDefault(targetId: Int) {
        var idNotFind = true
        val newScreenStack = screensStackKeys.mapNotNull {
            if (idNotFind) {
                if (it == targetId) {
                    idNotFind = false
                }
                it
            } else {
                null
            }
        }.takeIf { !idNotFind } ?: listOf(DEFAULT_SCREEN_ID)
        screensStackKeys.clear()
        screensStackKeys.addAll(newScreenStack)
    }

    @Serializable
    private sealed class Config(open val id: Int) {
        @Serializable
        data class MainScreenConfig(val ids: Int) : Config(ids)

        @Serializable
        data class BookInfoConfig(val ids: Int, val bookId: Long?) : Config(ids)

        @Serializable
        data class BookCreatorConfig(val ids: Int) : Config(ids)

        @Serializable
        data class ProfileConfig(val ids: Int) : Config(ids)

        @Serializable
        data class SettingsConfig(val ids: Int) : Config(ids)

        @Serializable
        data class BooksListInfoConfig(val ids: Int) : Config(ids)

        @Serializable
        data class ModerationConfig(val ids: Int) : Config(ids)
    }

    companion object {
        private const val DEFAULT_SCREEN_ID = 1
    }
}