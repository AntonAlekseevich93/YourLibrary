package review

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common_events.ReviewAndRatingEvents
import org.jetbrains.compose.resources.stringResource
import rating.elements.RatingBarElement
import text_fields.CommonTextField
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.review_text_field_hint

@Composable
fun BaseEventScope<BaseEvent>.WriteReviewScreen(
    bookName: String,
    userRating: Int?,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    var textState by remember { mutableStateOf(TextFieldValue()) }
    val charCount = remember { mutableStateOf(textState.text.length) }
    val minTextLength = remember { 70 }
    val charCountText = remember { mutableStateOf("${charCount.value}/$minTextLength") }
    var isActiveButton by remember { mutableStateOf(charCount.value >= minTextLength) }

    Column(
        modifier = modifier.fillMaxSize().background(ApplicationTheme.colors.cardBackgroundLight)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = bookName,
                style = ApplicationTheme.typography.title2Bold,
                color = ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.padding(top = 10.dp, bottom = 16.dp)
            )
            Text(
                text = "Вам понравилась книга?",
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.padding(bottom = 8.dp, top = 10.dp)
            )
            RatingBarElement(
                modifier = Modifier.padding(), iconSize = 26.dp,
                rating = userRating ?: 0,
                selectedRatingListener = {
                    sendEvent(ReviewAndRatingEvents.ChangeBookRating(newRating = it))
                }
            )
        }

        CommonTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 16.dp)
                .sizeIn(minHeight = 180.dp),
            textState = textState,
            onTextChanged = {
                textState = it
                val trimText = it.text.trim()
                val length = trimText.length
                charCount.value = length
                charCountText.value = "$length/$minTextLength"
                isActiveButton = length >= minTextLength
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = ApplicationTheme.colors.mainTextColor,
                disabledTextColor = ApplicationTheme.colors.cardBackgroundDark,
                backgroundColor = ApplicationTheme.colors.cardBackgroundDark,
            ),
            contentPadding = PaddingValues(16.dp),
            placeholder = {
                Text(
                    text = stringResource(Res.string.review_text_field_hint),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor
                )
            },
            verticalArrangement = Arrangement.Top,
            textStyle = ApplicationTheme.typography.headlineRegular.copy(textAlign = TextAlign.Start),
            shape = RoundedCornerShape(12.dp),
            maxLines = Int.MAX_VALUE
        )

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Text(
                text = "Минимальная длина отзыва",
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.hintColor,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = charCountText.value,
                style = ApplicationTheme.typography.captionRegular,
                color = ApplicationTheme.colors.hintColor,
            )
        }

        Button(
            onClick = {
                sendEvent(
                    ReviewAndRatingEvents.AddReview(
                        reviewText = textState.text
                    )
                )
            },
            enabled = isActiveButton,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ApplicationTheme.colors.screenColor.activeLinkColor,
                disabledBackgroundColor = ApplicationTheme.colors.pointerIsActiveCardColor
            ),
            modifier = modifier.fillMaxWidth().padding(32.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Оставить отзыв",
                    style = ApplicationTheme.typography.headlineBold,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            }
        }
    }
}