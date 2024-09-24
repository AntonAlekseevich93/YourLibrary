import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import book_info.BookInfoScreen
import bottom_app_bar.CustomBottomBar
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.essenty.backhandler.BackCallback
import components.modarations_books_screen.ModerationBooksScreen
import components.parsing_screens.SingleBookParsingScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import main_app_bar.MainAppBar
import main_models.TooltipItem
import navigation.RootComponent
import navigation.isBookCreatorScreen
import navigation.isMainScreen
import navigation.isProfileScreen
import navigation.isSettingsScreen
import platform.Platform
import platform.isMobile

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun Application(
    platform: Platform,
    desktopTooltip: MutableState<TooltipItem>? = null,
    platformDisplayHeight: Dp? = null,
    component: RootComponent,
    keyboardShown: State<Boolean>,
) {
    val viewModel = remember { Inject.instance<ApplicationViewModel>() }
    viewModel.component = component
    val shelfViewModel = remember { Inject.instance<ShelfViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val leftDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showSearch = remember { mutableStateOf(false) }
    val hazeBlurState = remember { HazeState() }
    val showSearchAppBarTextField = remember { mutableStateOf(false) }
    var showMainAppBar by remember { mutableStateOf(true) }
    var showBottomBar by remember { mutableStateOf(true) }
    val dbPathExist =
        remember { mutableStateOf(viewModel.isDbPathIsExist(platform)) } //todo dont delete. Вызывает краш неинециализации бд

    AppTheme {
        Scaffold(
            containerColor = ApplicationTheme.colors.mainBackgroundColor,
            topBar = {
                if (showMainAppBar) {
                    viewModel.MainAppBar(
                        hazeBlurState = hazeBlurState,
                        isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                        searchedBooks = uiState.searchedBooks,
                        platformDisplayHeight = platformDisplayHeight,
                        showSearchAppBarTextField = showSearchAppBarTextField,
                        changeVisibilitySearchAppBarTextField = {
                            showSearchAppBarTextField.value =
                                !showSearchAppBarTextField.value
                        }
                    )
                }
            },
            bottomBar = {
                if (!keyboardShown.value && platform.isMobile() && showBottomBar) {
                    CustomBottomBar(
                        hazeState = hazeBlurState,
                        isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                        navigationComponent = component
                    )
                }
            },
            modifier = Modifier.imePadding()
        ) { paddingValues ->
            component.backHandler.register(
                BackCallback {
                    component.onBackClicked()
                }
            )
            Children(
                stack = component.screenStack,
                animation = predictiveBackAnimation(
                    backHandler = component.backHandler,
                    fallbackAnimation = stackAnimation(slide()),
                    onBack = component::onBackClicked,
                ),
            ) {

                showMainAppBar = component.isMainScreen()
                showBottomBar =
                    component.isMainScreen() || component.isBookCreatorScreen()
                            || component.isProfileScreen() || component.isSettingsScreen()

                when (val screen = it.instance) {
                    is RootComponent.Screen.MainScreen -> {
                        var mainScreenModifier =
                            Modifier.pointerInput(showSearchAppBarTextField.value) {
                                if (showSearchAppBarTextField.value) {
                                    showSearchAppBarTextField.value = false
                                }
                            }

                        if (uiState.isHazeBlurEnabled.value) {
                            mainScreenModifier = mainScreenModifier.haze(
                                hazeBlurState,
                                HazeStyle(
                                    tint = Color.Black.copy(alpha = .2f),
                                    blurRadius = 30.dp,
                                )
                            )
                        }

                        MainScreen(
                            platform = platform,
                            showSearch = showSearch,
                            leftDrawerState = leftDrawerState,
                            shelfViewModel = shelfViewModel,
                            modifier = mainScreenModifier,
                            parentPaddingValues = paddingValues
                        )
                    }

                    is RootComponent.Screen.BookInfoScreen -> {
                        BookInfoScreen(navigationComponent = screen.component)
                    }

                    is RootComponent.Screen.BookCreatorScreen -> {
                        BookCreatorScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component
                        )
                    }

                    is RootComponent.Screen.UserBookCreatorScreen -> {
                        UserBookCreatorScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component,
                        )
                    }

                    is RootComponent.Screen.ProfileScreen -> {
                        ProfileScreen()
                    }

                    is RootComponent.Screen.SettingsScreen -> {
                        SettingsScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component,
                        )
                    }

                    is RootComponent.Screen.AdminScreen -> {
                        AdminPanelScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component
                        )
                    }

                    is RootComponent.Screen.ModerationScreen -> {
                        ModerationScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component
                        )
                    }

                    is RootComponent.Screen.ModerationBooksScreen -> {
                        ModerationBooksScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component
                        )
                    }

                    is RootComponent.Screen.ParsingScreen -> {
                        ParsingScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component
                        )
                    }

                    is RootComponent.Screen.SingleBookParsingScreen -> {
                        SingleBookParsingScreen(
                            hazeState = hazeBlurState,
                            navigationComponent = screen.component
                        )
                    }

                    is RootComponent.Screen.BooksListInfoScreen -> {
                        BooksListInfoScreen(
                            hazeState = hazeBlurState,
                            isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                            navigationComponent = screen.component,
                        )
                    }
                }
            }
        }
    }
}