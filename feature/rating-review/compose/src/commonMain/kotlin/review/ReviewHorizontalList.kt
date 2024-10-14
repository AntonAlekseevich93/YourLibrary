package review

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.rating_review.ReviewAndRatingVo
import org.jetbrains.compose.resources.pluralStringResource
import review.elements.ReviewHorizontalListItem
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.show_all_reviews

@Composable
fun ReviewHorizontalList(
    reviews: State<List<ReviewAndRatingVo>>,
    allReviewCount: Int,
    modifier: Modifier = Modifier,
    maxReviews: Int = 5,
    minReviewsForShowAllButton: Int = 3,
    onShowAllReviews: (scrollToReviewId: Int?) -> Unit,
) {
    val lazyState = rememberLazyListState()

    LazyRow(
        state = lazyState,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        itemsIndexed(reviews.value.take(maxReviews)) { index, review ->
            if (review.reviewText != null) {
                ReviewHorizontalListItem(
                    review,
                    modifier = Modifier.padding(
                        end = 16.dp,
                        start = if (index == 0) 16.dp else 0.dp
                    ),
                    showAllReview = onShowAllReviews
                )
            }
        }

        item {
            if (allReviewCount > minReviewsForShowAllButton) {
                Text(
                    text = pluralStringResource(
                        Res.plurals.show_all_reviews,
                        allReviewCount,
                        allReviewCount
                    ),
                    style = ApplicationTheme.typography.footnoteBold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(start = 26.dp, end = 20.dp)
                        .clickable(interactionSource = MutableInteractionSource(), null) {
                            onShowAllReviews(null)
                        }
                )
            }
        }
    }
}

