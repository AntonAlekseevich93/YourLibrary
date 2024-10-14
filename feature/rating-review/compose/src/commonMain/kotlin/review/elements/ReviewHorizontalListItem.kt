package review.elements

import ApplicationTheme
import DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import main_models.rating_review.ReviewAndRatingVo
import org.jetbrains.compose.resources.painterResource
import rating.elements.RatingBarElement
import text.ExpandableText
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_dislike
import yourlibrary.common.resources.generated.resources.ic_like
import java.util.Locale

@Composable
internal fun ReviewHorizontalListItem(
    review: ReviewAndRatingVo,
    modifier: Modifier = Modifier,
    showAllReview: (scrollToReviewId: Int?) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundLight),
        modifier = modifier.height(250.dp).width(320.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
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
                collapsedMaxLine = 6,
                onClick = {
                    showAllReview(review.id)
                },
                style = ApplicationTheme.typography.headlineRegular.copy(
                    textDirection = TextDirection.Content,
                    hyphens = Hyphens.Auto,
                    lineBreak = LineBreak.Paragraph
                ),
                textAlign = TextAlign.Justify,
                showMoreOrShowLessAsNewLine = true
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_like),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp).size(20.dp),
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
                    modifier = Modifier.padding(end = 10.dp).size(20.dp),
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                )

                Text(
                    text = review.dislikesCount.toString(),
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.hintColor,
                )
                Spacer(Modifier.weight(1f))
                if (!review.isApprovedReview) {
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
