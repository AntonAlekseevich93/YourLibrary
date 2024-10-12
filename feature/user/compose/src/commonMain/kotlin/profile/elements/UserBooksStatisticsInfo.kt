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
import models.UserBooksStatistics

@Composable
fun UserBooksStatisticsInfo(
    userBooksStatistics: State<UserBooksStatistics>
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            StatisticItem(
                text = "Всего книг",
                count = userBooksStatistics.value.allBooksCount,
                modifier = Modifier.weight(1f)
            )
            StatisticItem(
                text = "Читаю",
                count = userBooksStatistics.value.readingBooksCount,
                modifier = Modifier.weight(1f)
            )
            StatisticItem(
                text = "Прочитано",
                count = userBooksStatistics.value.doneBooksCount,
                modifier = Modifier.weight(1f)
            )

            StatisticItem(
                text = "Отложено",
                count = userBooksStatistics.value.deferredBooksCount,
                modifier = Modifier.weight(1f)
            )
        }
        StatisticProgressIndicator(
            finishedBooks = userBooksStatistics.value.finishedThisYearBooks,
            plannedBooks = userBooksStatistics.value.plannedThisYearBooks
        )

        StatisticItemClickable(
            text = "Отзывов",
            count = 4,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 16.dp),
        )

        StatisticItemClickable(
            text = "Улучений сервиса",
            count = 2,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
private fun StatisticProgressIndicator(
    finishedBooks: Int,
    plannedBooks: Int,
) {
    val progress = finishedBooks.toFloat() / plannedBooks.toFloat()
    val progressPercent = (progress * 100).toInt()
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
                text = "2/4 книг запланированных на 2024",
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
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Card(
        modifier = modifier
            .clip(shape)
            .clickable(MutableInteractionSource(), rememberRipple()) {

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