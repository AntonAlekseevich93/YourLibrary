package components.modarations_books_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import models.AdminEvents

@Composable
fun BaseEventScope<BaseEvent>.DatabaseScreen(
    hazeModifier: Modifier = Modifier,
    topPadding: Dp,
    bottomPadding: Dp,
) {

    val scrollableState = rememberScrollState()

    Column(modifier = hazeModifier.fillMaxSize().verticalScroll(scrollableState)) {
        Button(
            modifier = Modifier.padding(top = topPadding.plus(24.dp), start = 24.dp),
            onClick = {
                sendEvent(AdminEvents.ClearReviewAndRatingDb)
            }
        ) {
            Text(
                text = "Очистить рейтинг бд",
                style = ApplicationTheme.typography.footnoteBold,
                color = ApplicationTheme.colors.mainTextColor
            )
        }
        Spacer(Modifier.padding(bottomPadding))
    }

}

