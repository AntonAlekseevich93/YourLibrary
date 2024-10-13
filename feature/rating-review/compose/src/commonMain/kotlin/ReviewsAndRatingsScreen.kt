import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import navigation.screen_components.ReviewsAndRatingsScreenComponent

@Composable
fun ReviewsAndRatingsScreen(
    hazeState: HazeState? = null,
    navigationComponent: ReviewsAndRatingsScreenComponent? = null,
    isHazeBlurEnabled: Boolean,
) {
    val viewModel = remember { Inject.instance<ReviewAndRatingViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val hazeModifier: Modifier = if (isHazeBlurEnabled && hazeState != null) {
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
            ReviewsAndRatingsAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = isHazeBlurEnabled,
                title = "Отзывы",
                onBack = {
                    navigationComponent?.onBack()
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier.padding(top = 1.dp) //fixes haze bug
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = hazeModifier
            ) {

                item { Spacer(Modifier.padding(top = it.calculateTopPadding())) }

            }
        }
    }
}

@Preview
@Composable
fun UserServiceDevelopmentScreenPreview() {
    val lazyListState = rememberLazyListState()
    AppTheme() {
        Scaffold(
            topBar = {
                ReviewsAndRatingsAppBar(
                    hazeBlurState = null,
                    isHazeBlurEnabled = false,
                    title = "Отзывы",
                    onBack = {

                    }
                )
            },
            containerColor = ApplicationTheme.colors.cardBackgroundDark,
        ) {
            Column(
                modifier = Modifier.padding(top = 1.dp) //fixes haze bug
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                ) {

                    item { Spacer(Modifier.padding(top = it.calculateTopPadding())) }

                }
            }
        }
    }
}