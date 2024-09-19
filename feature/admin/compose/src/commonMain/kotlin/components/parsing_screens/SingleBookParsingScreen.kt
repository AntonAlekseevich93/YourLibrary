package components.parsing_screens

import AdminViewModel
import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.AdminPanelAppBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import navigation.screen_components.SingleBookParsingScreenComponent
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.single_book_parsing

@Composable
fun SingleBookParsingScreen(
    hazeState: HazeState,
    navigationComponent: SingleBookParsingScreenComponent,
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
                title = stringResource(Res.string.single_book_parsing),
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

            }
        }
    }
}