import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app_bars.UserCommonAppBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import main_models.mocks.books.BooksMock
import navigation.screen_components.UserDevelopmentServiceScreenComponent
import org.jetbrains.compose.resources.stringResource
import user_service_development.EmptyServiceDevelopmentScreen
import user_service_development.UserServiceDevelopmentBookItemScreen
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.service_development

@Composable
fun UserServiceDevelopmentScreen(
    hazeState: HazeState? = null,
    navigationComponent: UserDevelopmentServiceScreenComponent? = null,
    isHazeBlurEnabled: Boolean,
) {
    val lazyListState = rememberLazyListState()
    val viewModel = remember { Inject.instance<UserViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val hazeModifier: Modifier = if (isHazeBlurEnabled && hazeState != null) {
        Modifier.haze(
            state = hazeState,
            style = HazeStyle(
                tint = Color.Black.copy(alpha = .04f),
                blurRadius = 30.dp,
            )
        )
    } else Modifier
    Scaffold(
        topBar = {
            UserCommonAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = isHazeBlurEnabled,
                title = stringResource(Res.string.service_development),
                onBack = {
                    navigationComponent?.onBack()
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier.padding(top = 1.dp) //fixes haze bug
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = hazeModifier
            ) {

                item { Spacer(Modifier.padding(top = it.calculateTopPadding())) }

                item {
                    if (uiState.userBooksStatistics.value.serviceDevelopmentBooks.isEmpty()) {
                        EmptyServiceDevelopmentScreen()
                    } else {
                        Spacer(modifier = Modifier.padding(top = 24.dp))
                    }
                }

                items(uiState.userBooksStatistics.value.serviceDevelopmentBooks) {
                    UserServiceDevelopmentBookItemScreen(
                        bookWithServiceDevelopment = it,
                        openBookInfoListener = {
                            navigationComponent?.openBookInfoScreen(it.book.localId)
                        },
                        openEditBookListener = {
                            navigationComponent?.openServiceDevelopmentBookEditScreen()
                        }
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun UserServiceDevelopmentScreenPreview() {
    val lazyListState = rememberLazyListState()
//    val books = emptyList<BookWithServiceDevelopment>()
    val books = BooksMock.getListBookWithServiceDevelopment(
        size = 2,
        isApproved = false,
        isModerated = true
    )
    AppTheme() {
        Scaffold(
            topBar = {
                UserCommonAppBar(
                    hazeBlurState = null,
                    isHazeBlurEnabled = false,
                    title = stringResource(Res.string.service_development),
                    onBack = {
                    }
                )
            },
            containerColor = ApplicationTheme.colors.cardBackgroundDark,
        ) {
            Column(
                modifier = Modifier.padding(top = 1.dp) //fixes haze bug
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                ) {

                    item { Spacer(Modifier.padding(top = it.calculateTopPadding())) }

                    item {
                        if (books.isEmpty()) {
                            EmptyServiceDevelopmentScreen()
                        } else {
                            Spacer(modifier = Modifier.padding(top = 24.dp))
                        }
                    }

                    items(books) {
                        UserServiceDevelopmentBookItemScreen(it, asMock = true, {}, {})
                    }
                }
            }
        }
    }
}