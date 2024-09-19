import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.AdminPanelAppBar
import components.modarations_books_screen.ModerationBooksScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import navigation.screens.ModerationScreenComponent
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.moderation_books_title

@Composable
fun ModerationScreen(
    hazeState: HazeState,
    navigationComponent: ModerationScreenComponent
) {
    val viewModel = remember { Inject.instance<AdminViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdminPanelAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.moderation_books_title),
                showBackButton = true,
                onClose = {},
                onBack = {
                    navigationComponent.onBack()
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ApplicationTheme.colors.cardBackgroundDark)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .background(ApplicationTheme.colors.cardBackgroundDark)
                        .fillMaxSize()
                ) {
                    var moderationModifier: Modifier = Modifier
                    if (uiState.isHazeBlurEnabled.value) {
                        moderationModifier = moderationModifier.haze(
                            state = hazeState,
                            style = HazeStyle(
                                tint = Color.Black.copy(alpha = .04f),
                                blurRadius = 30.dp,
                            )
                        )
                    }
                    viewModel.ModerationBooksScreen(
                        state = uiState.moderationBookState,
                        hazeModifier = moderationModifier,
                        topPadding = it.calculateTopPadding(),
                        bottomPadding = 16.dp,
                    )
                }
            }
        }
    }
}