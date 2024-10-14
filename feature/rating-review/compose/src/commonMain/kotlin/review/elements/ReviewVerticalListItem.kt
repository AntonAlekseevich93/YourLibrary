package review.elements

import ApplicationTheme
import DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
internal fun ReviewVerticalListItem(
    review: ReviewAndRatingVo,
    modifier: Modifier = Modifier,
    expandedReviewIndex: State<Int> = mutableStateOf(-1),
    index: Int = -2,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundLight),
        modifier = modifier
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
                collapsedMaxLine = 8,
                textAlign = TextAlign.Justify,
                style = ApplicationTheme.typography.headlineRegular.copy(
                    textDirection = TextDirection.Content,
                    hyphens = Hyphens.Auto,
                    lineBreak = LineBreak.Paragraph
                ),
                showMoreOrShowLessAsNewLine = true,
                expandWhenIndex = expandedReviewIndex,
                index = index
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
