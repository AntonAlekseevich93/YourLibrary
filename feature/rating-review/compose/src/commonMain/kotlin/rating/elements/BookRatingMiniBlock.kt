package rating.elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import rating.CurrentUserRatingLabel
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.review_amount
import yourlibrary.common.resources.generated.resources.set_estimate

@Composable
fun BookRatingMiniBlock(
    allUsersRating: Double,
    allRatingAmount: Int,
    currentUserScore: Int?,
    scrollToReviewButtonListener: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Text(
                text = allUsersRating.takeIf { it > 0 }?.toString() ?: "0",
                style = ApplicationTheme.typography.headlineBold.copy(fontSize = 18.sp),
                color = ApplicationTheme.colors.mainTextColor,
            )
            if (currentUserScore != null && currentUserScore > 0) {
                CurrentUserRatingLabel(currentUserScore, modifier = Modifier.padding(start = 8.dp))
            }
        }

        if (allRatingAmount > 0) {
            Text(
                text = pluralStringResource(
                    Res.plurals.review_amount,
                    allRatingAmount,
                    allRatingAmount
                ),
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor,
            )
        } else {
            Text(
                text = stringResource(Res.string.set_estimate),
                style = ApplicationTheme.typography.footnoteRegular,
                color = Color(0xFFa2d2ff),
                modifier = Modifier.clickable { scrollToReviewButtonListener() }
            )
        }
    }
}