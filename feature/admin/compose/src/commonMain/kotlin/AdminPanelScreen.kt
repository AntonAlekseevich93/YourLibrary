import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import components.modarations_books_screen.AdminPanelAppBar
import components.modarations_books_screen.DatabaseScreen
import components.modarations_books_screen.ModerationBooksScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import models.AdminEvents
import org.jetbrains.compose.resources.stringResource
import text_fields.CommonTextField
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.admin_panel

@Composable
fun AdminPanelScreen(
    hazeState: HazeState,
) {
    val viewModel = remember { Inject.instance<AdminViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdminPanelAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.admin_panel),
                showBackButton = uiState.moderationBookState.selectedItem != null || uiState.databaseMenuScreen.value,
                onClose = {},
                onBack = {
                    viewModel.sendEvent(AdminEvents.OnBack)
                }
            )
        },
        containerColor = ApplicationTheme.colors.mainBackgroundColor,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ApplicationTheme.colors.mainBackgroundColor)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .background(ApplicationTheme.colors.mainBackgroundColor)
                        .fillMaxSize()
                ) {
                    AnimatedVisibility(visible = uiState.moderationBookState.selectedItem == null && !uiState.databaseMenuScreen.value) {
                        Column(
                            modifier = Modifier.padding(
                                top = it.calculateTopPadding().plus(24.dp), bottom = 24.dp
                            )
                        ) {
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

                            Text(
                                text = "Database menu",
                                style = ApplicationTheme.typography.bodyBold,
                                color = ApplicationTheme.colors.mainTextColor,
                                modifier = Modifier.padding(start = 24.dp, top = 12.dp).clickable {
                                    viewModel.sendEvent(AdminEvents.OpenDatabaseMenuScreen)
                                }
                            )

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

                    AnimatedVisibility(visible = uiState.moderationBookState.selectedItem != null) {
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
                            bottomPadding = it.calculateBottomPadding(),
                        )
                    }

                    AnimatedVisibility(visible = uiState.databaseMenuScreen.value) {
                        var modifier: Modifier = Modifier
                        if (uiState.isHazeBlurEnabled.value) {
                            modifier = modifier.haze(
                                state = hazeState,
                                style = HazeStyle(
                                    tint = Color.Black.copy(alpha = .04f),
                                    blurRadius = 30.dp,
                                )
                            )
                        }
                        viewModel.DatabaseScreen(
                            hazeModifier = modifier,
                            topPadding = it.calculateTopPadding(),
                            bottomPadding = it.calculateBottomPadding(),
                        )
                    }
                }

                if (uiState.moderationBookState.selectedItem == null) {
                    Column(
                        modifier = Modifier.padding(start = 10.dp, bottom = 44.dp, end = 10.dp)
                            .padding(bottom = it.calculateBottomPadding().plus(46.dp))
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
                                placeholder = {
                                    Text(
                                        text = "Custom url",
                                        color = ApplicationTheme.colors.mainTextColor,
                                        style = ApplicationTheme.typography.footnoteRegular
                                    )
                                },
                                unfocusedIndicatorLineThickness = 1.dp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Checkbox(
                                checked = uiState.useHttp,
                                onCheckedChange = {
                                    viewModel.sendEvent(AdminEvents.ChangeNeedUseHttp)
                                }
                            )
                            Text(
                                text = "Use HTTP",
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
                                checked = uiState.useNonModerationRange,
                                onCheckedChange = {
                                    viewModel.sendEvent(
                                        AdminEvents.ChangeNeedUseNonModerationRange(
                                            it
                                        )
                                    )
                                }
                            )

                            CommonTextField(
                                modifier = Modifier.weight(1f),
                                textState = uiState.rangeStart,
                                onTextChanged = {
                                    viewModel.sendEvent(AdminEvents.ChangeNonModerationStartRange(it))
                                },
                                unfocusedIndicatorLineThickness = 1.dp,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Text(
                                text = "-",
                                style = ApplicationTheme.typography.footnoteRegular,
                                color = ApplicationTheme.colors.mainTextColor,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                            CommonTextField(
                                modifier = Modifier.weight(1f),
                                textState = uiState.rangeEnd,
                                onTextChanged = {
                                    viewModel.sendEvent(AdminEvents.ChangeNonModerationEndRange(it))
                                },
                                unfocusedIndicatorLineThickness = 1.dp,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }
                }
            }
        }
    }
}