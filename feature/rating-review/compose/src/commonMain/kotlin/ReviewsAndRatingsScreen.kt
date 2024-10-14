import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import kotlinx.coroutines.launch
import main_models.mocks.reviews.ReviewsMock
import models.ReviewAndRatingEvents
import navigation.screen_components.ReviewsAndRatingsScreenComponent
import review.elements.ReviewVerticalListItem

@Composable
fun ReviewsAndRatingsScreen(
    hazeState: HazeState? = null,
    navigationComponent: ReviewsAndRatingsScreenComponent?,
    isHazeBlurEnabled: Boolean,
) {
    val viewModel = remember { navigationComponent!!.getRatingAndReviewViewModel() }
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
    val scope = rememberCoroutineScope()
    val indexExpandedReview = remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) {
        navigationComponent?.reviews?.let {
            viewModel.sendEvent(ReviewAndRatingEvents.SetReviews(it))
        }
        if (navigationComponent?.scrollToReviewId != null && lazyListState.firstVisibleItemIndex == 0) {
            indexExpandedReview.value =
                uiState.reviews.value.indexOfFirst { it.id == navigationComponent.scrollToReviewId }

            if (indexExpandedReview.value >= 0) {
                scope.launch {
                    lazyListState.scrollToItem(indexExpandedReview.value)
                }
            }
        } else {
            indexExpandedReview.value = -1
        }
    }

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

                itemsIndexed(
                    uiState.reviews.value,
                ) { index, item ->
                    ReviewVerticalListItem(
                        item,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        index = index,
                        expandedReviewIndex = indexExpandedReview
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun UserServiceDevelopmentScreenPreview() {
    val lazyListState = rememberLazyListState()
    val reviews = ReviewsMock.getReviews(size = 6)
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

                    items(reviews) {
                        ReviewVerticalListItem(
                            it,
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}