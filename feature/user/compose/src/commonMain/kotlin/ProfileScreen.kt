import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import buttons.MenuButton
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import navigation.screen_components.ProfileScreenComponent
import org.jetbrains.compose.resources.stringResource
import profile.ProfileContent
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.administration_panel
import yourlibrary.common.resources.generated.resources.ic_moderation_menu
import yourlibrary.common.resources.generated.resources.profile

@Composable
fun ProfileScreen(
    hazeState: HazeState,
    navigationComponent: ProfileScreenComponent,
    isHazeBlurEnabled: Boolean,
) {
    val viewModel = remember { Inject.instance<UserViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val hazeModifier: Modifier = if (isHazeBlurEnabled) {
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
            ProfileAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = isHazeBlurEnabled,
                title = stringResource(Res.string.profile),
                showBackButton = false,
                onSettings = {
                    navigationComponent.onSettingsClick()
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
                viewModel.ProfileContent(uiState.userInfo)

                MenuButton(
                    icon = Res.drawable.ic_moderation_menu,
                    iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                    invoke = {
                        Text(
                            text = stringResource(Res.string.administration_panel),
                            style = ApplicationTheme.typography.headlineRegular,
                            color = ApplicationTheme.colors.mainTextColor,

                            )
                    },
                    onClick = {
                        navigationComponent.openAdminPanel()
                    }
                )

            }
        }
    }
}