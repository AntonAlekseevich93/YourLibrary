package user_book_creator_screen

import ApplicationTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import text_fields.CommonTextField

@Composable
internal fun BookCreatorAuthorElement(
    isEnabled: Boolean,
    textState: MutableState<TextFieldValue>,
) {
    CommonTextField(
        labelText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                append("Автор")
            }

            withStyle(style = SpanStyle(color = ApplicationTheme.colors.readingStatusesColor.readingStatusColor)) {
                append("*")
            }
        },
        textState = textState,
        maxLines = 1,
        enabled = isEnabled,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
        imeAction = ImeAction.Done,
    )
}