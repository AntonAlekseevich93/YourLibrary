package components.modarations_books_screen.elements

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import models.AdminEvents
import text_fields.OldCommonTextField

@Composable
internal fun BaseEventScope<BaseEvent>.ChangeBookNameEditorElement(changedName: String) {
    val textField: MutableState<TextFieldValue> =
        remember {
            mutableStateOf(
                TextFieldValue(
                    text = changedName
                )
            )
        }
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        OldCommonTextField(
            modifier = Modifier,
            focusedIndicatorLineThickness = 1.dp,
            unfocusedIndicatorLineThickness = 1.dp,
            textState = textField.value,
            onTextChanged = {
                textField.value = it
            },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(
                text = "Сохранить",
                modifier = Modifier.clickable {
                    sendEvent(AdminEvents.OnSaveChangeBookName(textField.value.text.trim()))
                }.padding(end = 12.dp),
                style = ApplicationTheme.typography.footnoteBold,
                color = ApplicationTheme.colors.mainTextColor
            )

            Text(
                text = "Отменить",
                modifier = Modifier.clickable {
                    sendEvent(AdminEvents.OnCancelChangeBookName)
                }.padding(end = 12.dp),
                style = ApplicationTheme.typography.footnoteBold,
                color = ApplicationTheme.colors.mainTextColor
            )

            Text(
                text = "Удалить",
                modifier = Modifier.clickable {
                    sendEvent(AdminEvents.OnDeleteChangeBookName)
                },
                style = ApplicationTheme.typography.footnoteBold,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

    }
}