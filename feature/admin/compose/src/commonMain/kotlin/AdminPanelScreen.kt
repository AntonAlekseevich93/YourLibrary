import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import text_fields.CommonTextField
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
                .background(ApplicationTheme.colors.mainBackgroundColor)
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
                Column(modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)) {
                    Text(
                        text = "Получить книги для модерации",
                        style = ApplicationTheme.typography.bodyBold,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.padding(start = 24.dp).clickable {
                            viewModel.sendEvent(AdminEvents.GetBooksForModerating)
                        }
                    )

                    Text(
                        text = "Получить книги для модерации без необходимости загружать изображения",
                        style = ApplicationTheme.typography.bodyBold,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.padding(start = 24.dp, top = 12.dp).clickable {
                            viewModel.sendEvent(AdminEvents.GetBooksForModeratingWithoutUploadingImages)
                        }
                    )
                }
            }

            AnimatedVisibility(visible = uiState.moderationBookState.selectedItem != null) {
                viewModel.ModerationBooksScreen(
                    state = uiState.moderationBookState,
                )
            }
        }

        if (uiState.moderationBookState.selectedItem == null) {
            Column(
                modifier = Modifier.padding(start = 10.dp, bottom = 44.dp)
                    .align(Alignment.BottomStart)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = uiState.skipLongImageLoading,
                        onCheckedChange = {
                            viewModel.sendEvent(AdminEvents.ChangeSkipImageLongLoadingSettings)
                        }
                    )
                    Text(
                        text = "Пропускать долгую загрузку фотографий",
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Checkbox(
                        checked = uiState.useCustomHost,
                        onCheckedChange = {
                            viewModel.sendEvent(AdminEvents.ChangeNeedUseCustomUrl(it))
                        }
                    )

                    CommonTextField(
                        textState = uiState.customUrl,
                        onTextChanged = {
                            viewModel.sendEvent(AdminEvents.CustomUrlChanged(it))
                        },
                        unfocusedIndicatorLineThickness = 1.dp
                    )
                }
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