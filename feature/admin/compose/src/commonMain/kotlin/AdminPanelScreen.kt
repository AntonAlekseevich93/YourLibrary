import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import buttons.MenuButton
import components.AdminPanelAppBar
import components.DatabaseScreen
import components.NotificationsScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import models.AdminEvents
import navigation.screen_components.AdminScreenComponent
import org.jetbrains.compose.resources.stringResource
import text_fields.OldCommonTextField
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.admin_panel
import yourlibrary.common.resources.generated.resources.books_parsing
import yourlibrary.common.resources.generated.resources.database
import yourlibrary.common.resources.generated.resources.ic_code
import yourlibrary.common.resources.generated.resources.ic_database
import yourlibrary.common.resources.generated.resources.ic_moderation_menu
import yourlibrary.common.resources.generated.resources.moderation

@Composable
fun AdminPanelScreen(
    hazeState: HazeState,
    navigationComponent: AdminScreenComponent
) {
    val viewModel = remember { Inject.instance<AdminViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdminPanelAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.admin_panel),
                showCloseButton = false,
                onClose = {},
                onBack = {
                    if (uiState.notificationsScreen.value || uiState.databaseMenuScreen.value) {
                        uiState.notificationsScreen.value = false
                        uiState.databaseMenuScreen.value = false
                    } else {
                        navigationComponent.onBack()
                    }

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
                    AnimatedVisibility(visible = !uiState.databaseMenuScreen.value) {
                        Column(
                            modifier = Modifier.padding(
                                top = it.calculateTopPadding().plus(24.dp), bottom = 24.dp
                            )
                        ) {
                            MenuButton(
                                icon = Res.drawable.ic_moderation_menu,
                                iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                                invoke = {
                                    Text(
                                        text = stringResource(Res.string.moderation),
                                        style = ApplicationTheme.typography.headlineRegular,
                                        color = ApplicationTheme.colors.mainTextColor,

                                        )
                                },
                                onClick = {
                                    viewModel.sendEvent(AdminEvents.OnOpenModerationScreen)
                                }
                            )

                            MenuButton(
                                icon = Res.drawable.ic_code,
                                iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                                invoke = {
                                    Text(
                                        text = stringResource(Res.string.books_parsing),
                                        style = ApplicationTheme.typography.headlineRegular,
                                        color = ApplicationTheme.colors.mainTextColor,

                                        )
                                },
                                onClick = {
                                    navigationComponent.openParsingScreen()
                                }
                            )

                            MenuButton(
                                icon = Res.drawable.ic_database,
                                iconSize = 16.dp,
                                iconModifier = Modifier.padding(start = 2.dp, end = 1.dp),
                                iconColorFilter = ColorFilter.tint(
                                    ApplicationTheme.colors.mainIconsColor.copy(
                                        alpha = 0.8f
                                    )
                                ),
                                invoke = {
                                    Text(
                                        text = stringResource(Res.string.database),
                                        style = ApplicationTheme.typography.headlineRegular,
                                        color = ApplicationTheme.colors.mainTextColor,

                                        )
                                },
                                onClick = {
                                    viewModel.sendEvent(AdminEvents.OpenDatabaseMenuScreen)
                                }
                            )

                            MenuButton(
                                icon = Res.drawable.ic_moderation_menu,
                                iconSize = 16.dp,
                                iconModifier = Modifier.padding(start = 2.dp, end = 1.dp),
                                iconColorFilter = ColorFilter.tint(
                                    ApplicationTheme.colors.mainIconsColor.copy(
                                        alpha = 0.8f
                                    )
                                ),
                                invoke = {
                                    Text(
                                        text = "Уведомления",
                                        style = ApplicationTheme.typography.headlineRegular,
                                        color = ApplicationTheme.colors.mainTextColor,
                                    )
                                },
                                onClick = {
                                    uiState.notificationsScreen.value = true
                                }
                            )

                        }
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

                Column(
                    modifier = Modifier.padding(start = 10.dp, bottom = 44.dp, end = 10.dp)
                        .padding(bottom = it.calculateBottomPadding().plus(46.dp))
                        .align(Alignment.BottomStart)
                ) {
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

                        OldCommonTextField(
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

                        OldCommonTextField(
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

                        OldCommonTextField(
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

        AnimatedVisibility(visible = uiState.notificationsScreen.value) {
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
            viewModel.NotificationsScreen(
                hazeModifier = modifier,
                topPadding = it.calculateTopPadding(),
                bottomPadding = it.calculateBottomPadding(),
            )
        }
    }
}