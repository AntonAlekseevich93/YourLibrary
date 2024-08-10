package review.elements

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.has_not_review
import yourlibrary.common.resources.generated.resources.ic_branch
import yourlibrary.common.resources.generated.resources.review_amount

@Composable
fun ReviewAmountInfoElement(
    reviewCount: Int,
    averageRating: Double,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        if (reviewCount > 0 && averageRating > 0) {
            val ratingText =
                pluralStringResource(Res.plurals.review_amount, reviewCount, reviewCount)
            Row {
                Image(
                    painter = painterResource(Res.drawable.ic_branch),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                    modifier = Modifier
                        .size(36.dp)
                        .graphicsLayer {
                            rotationY = 180f
                        }.rotate(-33f)
                )
                Text(
                    text = averageRating.toString(),
                    style = ApplicationTheme.typography.title1Bold.copy(fontSize = 36.sp),
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(bottom = 4.dp, top = 4.dp)
                )
                Image(
                    painter = painterResource(Res.drawable.ic_branch),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                    modifier = Modifier
                        .size(36.dp)
                        .rotate(-33f)
                )
            }
            Text(
                text = ratingText,
                style = ApplicationTheme.typography.captionRegular,
                color = ApplicationTheme.colors.hintColor,
            )
        } else {
            Text(
                text = "0",
                style = ApplicationTheme.typography.title1Bold.copy(fontSize = 36.sp),
                color = ApplicationTheme.colors.hintColor,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(Res.string.has_not_review),
                style = ApplicationTheme.typography.captionRegular,
                color = ApplicationTheme.colors.hintColor,
            )
        }
    }
}