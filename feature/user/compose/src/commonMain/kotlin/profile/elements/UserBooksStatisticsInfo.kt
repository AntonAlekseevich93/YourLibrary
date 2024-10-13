package profile.elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import main_models.books.UserBooksStatisticsData
import main_models.rating_review.ReviewAndRatingVo
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.reading_status_deferred
import yourlibrary.common.resources.generated.resources.reading_status_done
import yourlibrary.common.resources.generated.resources.reading_status_is_reading
import yourlibrary.common.resources.generated.resources.reading_status_planned
import yourlibrary.common.resources.generated.resources.user_books_statistics_planned

@Composable
fun UserBooksStatisticsInfo(
    userBooksStatistics: State<UserBooksStatisticsData>,
    userReviews: State<List<ReviewAndRatingVo>>,
    onServiceDevelopmentClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            StatisticItem(
                text = stringResource(Res.string.reading_status_planned),
                count = userBooksStatistics.value.plannedBooksCount,
                modifier = Modifier.weight(1f)
            )
            StatisticItem(
                text = stringResource(Res.string.reading_status_is_reading),
                count = userBooksStatistics.value.readingBooksCount,
                modifier = Modifier.weight(1f)
            )
            StatisticItem(
                text = stringResource(Res.string.reading_status_done),
                count = userBooksStatistics.value.doneBooksCount,
                modifier = Modifier.weight(1f)
            )

            StatisticItem(
                text = stringResource(Res.string.reading_status_deferred),
                count = userBooksStatistics.value.deferredBooksCount,
                modifier = Modifier.weight(1f)
            )
        }
        StatisticProgressIndicator(
            finishedBooks = userBooksStatistics.value.finishedThisYearBooks,
            plannedBooks = userBooksStatistics.value.plannedThisYearBooks,
            year = userBooksStatistics.value.currentYear
        )

        StatisticItemClickable(
            text = "Отзывов",
            count = userReviews.value.size,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 16.dp),
            onClick = {}
        )

        StatisticItemClickable(
            text = "Улучений сервиса",
            count = userBooksStatistics.value.serviceDevelopmentBooks.size,
            modifier = Modifier.fillMaxWidth(),
            onClick = onServiceDevelopmentClick

        )

    }
}

@Composable
private fun StatisticProgressIndicator(
    finishedBooks: Int,
    plannedBooks: Int,
    year: Int
) {
    val progress = if (finishedBooks > 0 && plannedBooks > 0) {
        (finishedBooks.toFloat() / plannedBooks.toFloat())
    } else {
        0f
    }
    val progressPercent = if (progress > 0) (progress * 100).toInt() else 0
    val shape = RoundedCornerShape(12.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp)
            .clip(shape)
            .clickable(MutableInteractionSource(), rememberRipple()) {

            },
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.mainBackgroundColor),
        shape = shape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(
                    Res.string.user_books_statistics_planned,
                    plannedBooks,
                    finishedBooks,
                    year
                ),
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor,
                softWrap = true,
                letterSpacing = TextUnit.Unspecified,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp, bottom = 16.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color(0xFFffbe0b),
                    trackColor = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor
                )

                Text(
                    text = "$progressPercent%",
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                    softWrap = true,
                    letterSpacing = TextUnit.Unspecified,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

        }
    }

}

@Composable
private fun StatisticItem(text: String, count: Int, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = count.toString(),
            style = ApplicationTheme.typography.headlineBold.copy(fontSize = 18.sp),
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = text,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            softWrap = true,
            letterSpacing = TextUnit.Unspecified,
        )
    }
}

@Composable
private fun StatisticItemClickable(
    text: String,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)
    Card(
        modifier = modifier
            .clip(shape)
            .clickable(MutableInteractionSource(), rememberRipple()) {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.mainBackgroundColor),
        shape = shape
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = count.toString(),
                    style = ApplicationTheme.typography.headlineBold.copy(fontSize = 18.sp),
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = text,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                    softWrap = true,
                    letterSpacing = TextUnit.Unspecified,
                )
            }
            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 10.dp).size(16.dp),
                    tint = ApplicationTheme.colors.mainIconsColor,
                )
            }
        }
    }
}