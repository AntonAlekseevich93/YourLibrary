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
import main_models.books.BookShortVo
import navigation.screen_components.AdminScreenComponent
import navigation.screen_components.BookCreatorScreenComponent
import navigation.screen_components.BookInfoComponent
import navigation.screen_components.BooksListInfoScreenComponent
import navigation.screen_components.DefaultAdminScreenComponent
import navigation.screen_components.DefaultBookCreatorScreenComponent
import navigation.screen_components.DefaultBookInfoComponent
import navigation.screen_components.DefaultBooksListInfoScreenComponent
import navigation.screen_components.DefaultMainScreenComponent
import navigation.screen_components.DefaultModerationBooksCoversScreenComponent
import navigation.screen_components.DefaultModerationBooksScreenComponent
import navigation.screen_components.DefaultModerationScreenComponent
import navigation.screen_components.DefaultParsingScreenComponent
import navigation.screen_components.DefaultProfileScreenComponent
import navigation.screen_components.DefaultSettingsScreenComponent
import navigation.screen_components.DefaultSingleBookParsingScreenComponent
import navigation.screen_components.DefaultUserBookCreatorScreenComponent
import navigation.screen_components.MainScreenComponent
import navigation.screen_components.ModerationBooksCoversScreenComponent
import navigation.screen_components.ModerationBooksScreenComponent
import navigation.screen_components.ModerationScreenComponent
import navigation.screen_components.ParsingScreenComponent
import navigation.screen_components.ProfileScreenComponent
import navigation.screen_components.SettingsScreenComponent
import navigation.screen_components.SingleBookParsingScreenComponent
import navigation.screen_components.UserBookCreatorScreenComponent

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
        class UserBookCreatorScreen(val component: UserBookCreatorScreenComponent) : Screen()
        class ProfileScreen(val component: ProfileScreenComponent) : Screen()
        class SettingsScreen(val component: SettingsScreenComponent) : Screen()
        class BooksListInfoScreen(val component: BooksListInfoScreenComponent) : Screen()
        class ModerationScreen(val component: ModerationScreenComponent) : Screen()
        class ModerationBooksScreen(val component: ModerationBooksScreenComponent) : Screen()
        class ModerationBooksCoversScreen(val component: ModerationBooksCoversScreenComponent) :
            Screen()

        class AdminScreen(val component: AdminScreenComponent) : Screen()
        class SingleBookParsingScreen(val component: SingleBookParsingScreenComponent) : Screen()
        class ParsingScreen(val component: ParsingScreenComponent) : Screen()
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
    private var bookListInfoFirstScreenId: Int = DEFAULT_SCREEN_ID

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

            is Config.UserBookCreatorConfig -> RootComponent.Screen.UserBookCreatorScreen(
                itemUserBookCreatorScreen(componentContext)
            )

            is Config.ProfileConfig -> RootComponent.Screen.ProfileScreen(
                itemProfileScreen(componentContext)
            )

            is Config.SettingsConfig -> RootComponent.Screen.SettingsScreen(
                itemSettingsScreen(componentContext)
            )

            is Config.BooksListInfoConfig -> RootComponent.Screen.BooksListInfoScreen(
                itemBooksListInfoScreen(
                    componentContext,
                    authorId = config.authorId,
                    screenTitle = config.screenTitle,
                    books = config.books
                )
            )

            is Config.ModerationConfig -> RootComponent.Screen.ModerationScreen(
                itemModerationScreen(componentContext)
            )

            is Config.ModerationBooksConfig -> RootComponent.Screen.ModerationBooksScreen(
                itemModerationBooksScreen(componentContext)
            )

            is Config.ModerationBooksCoversConfig -> RootComponent.Screen.ModerationBooksCoversScreen(
                itemModerationBooksCoversScreen(componentContext)
            )

            is Config.AdminConfig -> RootComponent.Screen.AdminScreen(
                itemAdminScreen(componentContext)
            )

            is Config.ParsingConfig -> RootComponent.Screen.ParsingScreen(
                itemParsingScreen(componentContext)
            )

            is Config.SingleBookParsingConfig -> RootComponent.Screen.SingleBookParsingScreen(
                itemSingleBookParsingScreen(componentContext)
            )
        }

    private fun itemMainScreen(componentContext: ComponentContext): MainScreenComponent =
        DefaultMainScreenComponent(
            componentContext = componentContext,
            showBookInfo = { bookId, shortBook, needSaveScreenId ->
                if (needSaveScreenId) {
                    bookInfoFirstScreenId = getCurrentStackKey
                }
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(
                        id,
                        bookId,
                        shortVo = shortBook,
                        previousScreenIsBookInfo = false
                    )
                )
            },
        )

    private fun itemBookInfo(
        componentContext: ComponentContext,
        config: Config.BookInfoConfig
    ): BookInfoComponent =
        DefaultBookInfoComponent(
            previousScreenIsBookInfo = config.previousScreenIsBookInfo,
            componentContext = componentContext,
            onBack = { pop() },
            showBookInfo = { bookId, shortBook ->
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(
                        id,
                        bookId,
                        shortVo = shortBook,
                        previousScreenIsBookInfo = true
                    )
                )
            },
            bookId = config.bookId,
            onCloseScreen = {
                popUntilStackIdFindOrFirstScreen(bookInfoFirstScreenId)
                bookInfoFirstScreenId = DEFAULT_SCREEN_ID
            },
            shortBook = config.shortVo,
            openAuthorsBooksScreen = { screenTitle, authorId, books, needSaveScreenId ->
                if (needSaveScreenId) {
                    bookListInfoFirstScreenId = getCurrentStackKey
                }
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BooksListInfoConfig(
                        ids = id,
                        screenTitle = screenTitle,
                        authorId = authorId,
                        books = books
                    )
                )
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
                    config = Config.BookInfoConfig(
                        id,
                        bookId,
                        shortVo = shortBook,
                        previousScreenIsBookInfo = false
                    )
                )
            },
            showUserBookCreatorScreen = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.UserBookCreatorConfig(id)
                )
            }
        )

    private fun itemUserBookCreatorScreen(componentContext: ComponentContext): UserBookCreatorScreenComponent =
        DefaultUserBookCreatorScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            toMainListener = {
                menuClick(MenuItem.MAIN)
            }
        )

    private fun itemProfileScreen(componentContext: ComponentContext): ProfileScreenComponent =
        DefaultProfileScreenComponent(
            componentContext = componentContext,
        )

    private fun itemSettingsScreen(componentContext: ComponentContext): SettingsScreenComponent =
        DefaultSettingsScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            onOpenAdminPanelListener = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.AdminConfig(id)
                )
            }
        )

    private fun itemBooksListInfoScreen(
        componentContext: ComponentContext,
        authorId: String?,
        books: List<BookShortVo>,
        screenTitle: String,
    ): BooksListInfoScreenComponent =
        DefaultBooksListInfoScreenComponent(
            componentContext = componentContext,
            authorId = authorId,
            screenTitle = screenTitle,
            books = books,
            onBackListener = {
                pop()
            },
            openBookInfoScreen = { bookId, shortBook ->
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(
                        ids = id,
                        bookId = bookId,
                        shortVo = shortBook,
                        previousScreenIsBookInfo = true
                    )
                )
            },
            onCloseListener = {
                popUntilStackIdFindOrFirstScreen(bookListInfoFirstScreenId)
                bookListInfoFirstScreenId = DEFAULT_SCREEN_ID
            }
        )

    private fun itemModerationScreen(componentContext: ComponentContext): ModerationScreenComponent =
        DefaultModerationScreenComponent(
            componentContext = componentContext,
            openModerationBooksScreenEvent = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.ModerationBooksConfig(id)
                )
            },
            openModerationBooksCoversScreenEvent = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.ModerationBooksCoversConfig(id)
                )
            },
            onBackListener = {
                pop()
            },
            onCloseScreenListener = {
                popUntilStackIdFindOrFirstScreen(DEFAULT_SCREEN_ID)
            }
        )

    private fun itemModerationBooksScreen(componentContext: ComponentContext): ModerationBooksScreenComponent =
        DefaultModerationBooksScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            onCloseScreenListener = {
                popUntilStackIdFindOrFirstScreen(DEFAULT_SCREEN_ID)
            }
        )

    private fun itemModerationBooksCoversScreen(componentContext: ComponentContext): ModerationBooksCoversScreenComponent =
        DefaultModerationBooksCoversScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            onCloseScreenListener = {
                popUntilStackIdFindOrFirstScreen(DEFAULT_SCREEN_ID)
            }
        )

    private fun itemAdminScreen(componentContext: ComponentContext): AdminScreenComponent =
        DefaultAdminScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            onOpenParsingScreen = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.ParsingConfig(id)
                )
            },
            openModerationScreenCallback = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.ModerationConfig(id)
                )
            }
        )

    private fun itemParsingScreen(componentContext: ComponentContext): ParsingScreenComponent =
        DefaultParsingScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            onCloseScreenListener = {
                popUntilStackIdFindOrFirstScreen(DEFAULT_SCREEN_ID)
            },
            onOpenSingleBookParsingScreen = {
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.SingleBookParsingConfig(id)
                )
            }
        )

    private fun itemSingleBookParsingScreen(componentContext: ComponentContext): SingleBookParsingScreenComponent =
        DefaultSingleBookParsingScreenComponent(
            componentContext = componentContext,
            onBackListener = {
                pop()
            },
            onCloseScreenListener = {
                popUntilStackIdFindOrFirstScreen(DEFAULT_SCREEN_ID)
            },
            showBookInfo = { bookId, shortBook, _ ->
                bookInfoFirstScreenId = getCurrentStackKey
                val id = getNextStackKey
                push(
                    id = id,
                    config = Config.BookInfoConfig(
                        id,
                        bookId,
                        shortVo = shortBook,
                        previousScreenIsBookInfo = false
                    )
                )
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
        data class BookInfoConfig(
            val ids: Int,
            val bookId: Long?,
            val shortVo: BookShortVo?,
            val previousScreenIsBookInfo: Boolean
        ) : Config(ids)

        @Serializable
        data class BookCreatorConfig(val ids: Int) : Config(ids)

        @Serializable
        data class UserBookCreatorConfig(val ids: Int) : Config(ids)

        @Serializable
        data class ProfileConfig(val ids: Int) : Config(ids)

        @Serializable
        data class SettingsConfig(val ids: Int) : Config(ids)

        @Serializable
        data class BooksListInfoConfig(
            val ids: Int,
            val authorId: String?,
            val screenTitle: String,
            val books: List<BookShortVo>
        ) : Config(ids)

        @Serializable
        data class ModerationConfig(val ids: Int) : Config(ids)

        @Serializable
        data class ModerationBooksConfig(val ids: Int) : Config(ids)

        @Serializable
        data class ModerationBooksCoversConfig(val ids: Int) : Config(ids)

        @Serializable
        data class AdminConfig(val ids: Int) : Config(ids)

        @Serializable
        data class SingleBookParsingConfig(val ids: Int) : Config(ids)

        @Serializable
        data class ParsingConfig(val ids: Int) : Config(ids)
    }

    companion object {
        private const val DEFAULT_SCREEN_ID = 1
    }
}