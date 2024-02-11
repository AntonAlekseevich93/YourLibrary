package join_authors_screen

import ApplicationTheme
import AuthorsViewModel
import Strings
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import di.Inject
import platform.Platform
import toolbar.CommonToolbar
import toolbar.ToolbarEvents

@Composable
fun JoinAuthorsScreen(
    showLeftDrawer: State<Boolean>
) {
    val viewModel = remember { Inject.instance<AuthorsViewModel>() }
    val platform = remember { viewModel.getPlatform() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollableState = rememberScrollState()

    val targetVerticalPadding =
        if (platform == Platform.MOBILE) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (platform == Platform.MOBILE) 0.dp else if (showLeftDrawer.value) 100.dp else 220.dp

    val animatedVerticalPadding by animateDpAsState(
        targetValue = targetVerticalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )
    val animatedHorizontalPadding by animateDpAsState(
        targetValue = targetHorizontalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )

    val shapeInDp = 8.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        viewModel.sendEvent(ToolbarEvents.OnCloseEvent)
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(shapeInDp),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = animatedVerticalPadding,
                    horizontal = animatedHorizontalPadding
                )

                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            /** это нужно чтобы перехватывать onPress
                             * на корневом Box который закрывает поиск*/
                        },
                    )
                },
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollableState)
            ) {
                viewModel.CommonToolbar(
                    showLeftDrawer,
                    hideMainButtons = true,
                    title = Strings.relatesAuthors
                ) {}

                viewModel.JoinAuthorsContent(
                    state = uiState.joiningAuthorsUiState,
                    searchingAuthorsResult = uiState.searchingAuthorResult
                )
            }
        }
    }
}