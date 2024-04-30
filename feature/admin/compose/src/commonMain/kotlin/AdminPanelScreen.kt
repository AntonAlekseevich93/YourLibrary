import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.ModerationBooksScreen
import di.Inject
import models.AdminEvents
import toolbar.CommonToolbar

@Composable
fun AdminPanelScreen(
    showLeftDrawer: State<Boolean>,
) {
    val viewModel = remember { Inject.instance<AdminViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            viewModel.CommonToolbar(showLeftDrawer) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = Strings.admin_panel,
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            }

            AnimatedVisibility(visible = uiState.moderationBookState.selectedItem == null) {
                Text(
                    text = "Получить книги для модерации",
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(24.dp).clickable {
                        viewModel.sendEvent(AdminEvents.GetBooksForModerating)
                    }
                )
            }

            AnimatedVisibility(visible = uiState.moderationBookState.selectedItem != null) {
                viewModel.ModerationBooksScreen(
                    state = uiState.moderationBookState,
                )
            }
        }

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(ApplicationTheme.colors.mainBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ApplicationTheme.colors.hintColor)
            }
        }
    }
}