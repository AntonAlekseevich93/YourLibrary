package review

import ApplicationTheme
import DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import main_models.rating_review.ReviewAndRatingVo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import rating.elements.RatingBarElement
import text.ExpandableText
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_dislike
import yourlibrary.common.resources.generated.resources.ic_like
import yourlibrary.common.resources.generated.resources.show_all_reviews
import java.util.Locale

@Composable
fun ReviewHorizontalList(
    reviews: State<List<ReviewAndRatingVo>>,
    allReviewCount: Int,
    modifier: Modifier = Modifier,
    maxReviews: Int = 3,
    showAllReviewListener: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier.horizontalScroll(scrollState).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        reviews.value.fastForEachIndexed { index, review ->
            if (index + 1 > maxReviews) {
                return@fastForEachIndexed
            }
            if (review.reviewText != null) {
                ReviewHorizontalListItem(review, modifier = Modifier.padding(end = 16.dp))
            }
        }
        if (allReviewCount > maxReviews) {
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
                        showAllReviewListener()
                    }
            )
        }
    }
}

@Composable
internal fun ReviewHorizontalListItem(
    review: ReviewAndRatingVo,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundLight),
        modifier = modifier.height(220.dp).width(320.dp)
    ) {
        Column(Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dateInString = DateUtils.getDateInStringFromMillis(
                    review.timestampOfCreatingReview,
                    locale = Locale.ROOT
                )
                ReviewUserWithDateInfo(
                    userName = review.userName,
                    date = dateInString,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )
                RatingBarElement(iconSize = 20.dp, rating = review.ratingScore)
            }

            ExpandableText(
                text = review.reviewText!!,
                disableOnClick = true,
                collapsedMaxLine = 4,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_like),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp).size(24.dp),
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                )
                Text(
                    text = review.likesCount.toString(),
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.hintColor,
                    modifier = Modifier.padding(end = 22.dp)
                )

                Image(
                    painter = painterResource(Res.drawable.ic_dislike),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp).size(24.dp),
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                )

                Text(
                    text = review.dislikesCount.toString(),
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.hintColor,
                )
                Spacer(Modifier.weight(1f))
                if(!review.isApprovedReview) {
                    Text(
                        text = "На модерации",
                        style = ApplicationTheme.typography.bodyRegular,
                        color = ApplicationTheme.colors.screenColor.activeLinkColor,
                    )
                }
            }
        }
    }
}

@Composable
internal fun ReviewUserWithDateInfo(
    userName: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = userName,
            style = ApplicationTheme.typography.footnoteBold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = date,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.hintColor
        )
    }
}