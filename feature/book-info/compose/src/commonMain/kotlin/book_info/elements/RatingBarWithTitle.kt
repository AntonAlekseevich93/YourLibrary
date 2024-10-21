package book_info.elements

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import common_events.ReviewAndRatingEvents
import org.jetbrains.compose.resources.stringResource
import rating.elements.RatingBarElement
import review.elements.AddNewReviewButton
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.rating_bar_title

@Composable
internal fun BaseEventScope<BaseEvent>.AboutRating(
    showReviewButton: Boolean,
    userRating: Int,
    reviewButtonPosition: (position: Int) -> Unit,
    onClickReviewButton: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.rating_bar_title),
            style = ApplicationTheme.typography.bodyRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        RatingBarElement(
            rating = userRating,
            selectedRatingListener = {
                sendEvent(ReviewAndRatingEvents.ChangeBookRating(newRating = it))
            }
        )
    }

    if (showReviewButton) {
        AddNewReviewButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp, start = 16.dp, end = 16.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    reviewButtonPosition(layoutCoordinates.positionInParent().y.toInt())
                },
            onClick = onClickReviewButton
        )
    }
}