package components

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import models.AdminEvents
import text_fields.CommonTextField

@Composable
fun BaseEventScope<BaseEvent>.NotificationsScreen(
    hazeModifier: Modifier = Modifier,
    topPadding: Dp,
    bottomPadding: Dp,
) {
    val scrollableState = rememberScrollState()
    val titleTextField = remember { mutableStateOf(TextFieldValue()) }
    val bodyTextField = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = hazeModifier.fillMaxSize().background(ApplicationTheme.colors.cardBackgroundDark)
            .verticalScroll(scrollableState)
    ) {
        Spacer(Modifier.padding(top = topPadding))
        CommonTextField(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            label = "Заголовок",
            textState = titleTextField,
        )

        CommonTextField(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 26.dp),
            label = "Описание",
            textState = bodyTextField,
        )

        Button(
            onClick = {
                sendEvent(
                    AdminEvents.SendTestNotification(
                        title = titleTextField.value.text,
                        body = bodyTextField.value.text
                    )
                )
                titleTextField.value = TextFieldValue()
                bodyTextField.value = TextFieldValue()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ApplicationTheme.colors.screenColor.activeButtonColor,
                disabledBackgroundColor = ApplicationTheme.colors.pointerIsActiveCardColor
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = titleTextField.value.text.isNotEmpty() && bodyTextField.value.text.isNotEmpty()
        ) {
            Text(
                text = "Отправить уведомление",
                style = ApplicationTheme.typography.title3Bold,
                color = ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.padding(bottomPadding))
    }
}

