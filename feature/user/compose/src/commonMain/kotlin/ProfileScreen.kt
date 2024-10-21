import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app_bars.ProfileAppBar
import bottom_sheets.CommonBottomSheet
import containters.CenterColumnContainer
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import models.UserEvents
import models.getUserUiStateMock
import navigation.screen_components.ProfileScreenComponent
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import profile.ProfileContent
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.change
import yourlibrary.common.resources.generated.resources.days_in_read
import yourlibrary.common.resources.generated.resources.planed_books_in_year
import yourlibrary.common.resources.generated.resources.profile
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    hazeState: HazeState? = null,
    navigationComponent: ProfileScreenComponent? = null,
    isHazeBlurEnabled: Boolean,
) {
    val viewModel = remember { Inject.instance<UserViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val hazeModifier: Modifier = if (isHazeBlurEnabled && hazeState != null) {
        Modifier.haze(
            state = hazeState,
            style = HazeStyle(
                tint = Color.Black.copy(alpha = .04f),
                blurRadius = 30.dp,
            )
        )
    } else Modifier
    var showChangeBooksGoalBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ProfileAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = isHazeBlurEnabled,
                title = stringResource(Res.string.profile),
                showBackButton = false,
                onSettings = {
                    navigationComponent?.onSettingsClick()
                },
                onBack = {
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 1.dp) //fixes haze blur bug
                .verticalScroll(scrollState)
                .background(Color.Transparent)
        ) {
            Column(
                modifier = hazeModifier.fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            ) {
                ProfileContent(
                    uiState = uiState,
                    onAdminPanelClick = {
                        navigationComponent?.openAdminPanel()
                    },
                    onSignOut = {
                        viewModel.sendEvent(UserEvents.OnSignOut)
                    },
                    onServiceDevelopmentClick = {
                        navigationComponent?.openServiceDevelopmentScreen()
                    },
                    onReadingGoalsClick = {
                        showChangeBooksGoalBottomSheet = true
                    }
                )
                Spacer(Modifier.padding(64.dp))

            }
        }

        if (showChangeBooksGoalBottomSheet) {
            val state = remember {
                SliderState(
                    value = uiState.userInfo.value.userReadingGoalsInYears?.goals?.firstOrNull {
                        it.year == Calendar.getInstance().get(Calendar.YEAR)
                    }?.booksGoal?.toFloat() ?: 0f,
                    valueRange = 0f..60f
                )
            }
            CommonBottomSheet(
                buttonTitle = Res.string.change,
                onAccept = {
                    viewModel.sendEvent(UserEvents.ChangeBooksGoal(state.value.toInt()))
                    showChangeBooksGoalBottomSheet = false
                },
                onDismiss = {
                    showChangeBooksGoalBottomSheet = false
                },
                content = {
                    CenterColumnContainer(
                        modifier = Modifier.padding(
                            top = 16.dp,
                            bottom = 16.dp
                        )
                    ) {
                        Text(
                            "%.0f".format(state.value),
                            modifier = Modifier.padding(bottom = 10.dp),
                            style = ApplicationTheme.typography.title1Bold,
                            color = ApplicationTheme.colors.mainTextColor
                        )

                        Text(
                            stringResource(Res.string.planed_books_in_year),
                            modifier = Modifier,
                            style = ApplicationTheme.typography.headlineRegular,
                            color = ApplicationTheme.colors.mainTextColor
                        )

                        if (state.value > 0) {
                            val booksCount = state.value.toInt()
                            if (booksCount > 0) {
                                val days = 365 / booksCount
                                val text =
                                    pluralStringResource(Res.plurals.days_in_read, days, days)
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(top = 8.dp),
                                    style = ApplicationTheme.typography.footnoteRegular,
                                    color = ApplicationTheme.colors.mainTextColor
                                )
                            }
                        }

                    }

                    Slider(
                        state,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color(0xFFffbe0b),
                            thumbColor = Color(0xFFffbe0b)
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val scrollState = rememberScrollState()
    AppTheme() {
        Scaffold(
            topBar = {
                ProfileAppBar(
                    hazeBlurState = null,
                    isHazeBlurEnabled = false,
                    title = stringResource(Res.string.profile),
                    showBackButton = false,
                    onSettings = {

                    },
                    onBack = {
                    },
                )
            },
            containerColor = ApplicationTheme.colors.cardBackgroundDark,
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 1.dp) //fixes haze blur bug
                    .verticalScroll(scrollState)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(
                            top = it.calculateTopPadding(),
                            bottom = it.calculateBottomPadding()
                        )
                ) {
                    ProfileContent(uiState = getUserUiStateMock(), {}, {}, {}, {})
                }
            }
        }
    }
}
