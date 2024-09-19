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
import components.AdminPanelAppBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import navigation.screen_components.ParsingScreenComponent
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_code
import yourlibrary.common.resources.generated.resources.parsing
import yourlibrary.common.resources.generated.resources.single_book_parsing

@Composable
fun ParsingScreen(
    hazeState: HazeState,
    navigationComponent: ParsingScreenComponent,
) {
    val viewModel = remember { Inject.instance<AdminViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    var hazeModifier: Modifier = Modifier
    if (uiState.isHazeBlurEnabled.value) {
        hazeModifier = hazeModifier.haze(
            state = hazeState,
            style = HazeStyle(
                tint = Color.Black.copy(alpha = .04f),
                blurRadius = 30.dp,
            )
        )
    }
    val scrollableState = rememberScrollState()
    Scaffold(
        topBar = {
            AdminPanelAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.parsing),
                onClose = {
                    navigationComponent.onCloseScreen()
                },
                onBack = {
                    navigationComponent.onBack()
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = hazeModifier
                .fillMaxSize()
                .background(ApplicationTheme.colors.cardBackgroundDark)
                .verticalScroll(scrollableState)
        ) {
            Column(
                modifier = Modifier.padding(top = it.calculateTopPadding().plus(16.dp))
                    .fillMaxSize()
            ) {
                MenuButton(
                    icon = Res.drawable.ic_code,
                    iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                    invoke = {
                        Text(
                            text = stringResource(Res.string.single_book_parsing),
                            style = ApplicationTheme.typography.headlineRegular,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                    },
                    onClick = {
                        navigationComponent.openSingleParsingScreen()
                    }
                )
            }
        }
    }
}