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
import org.jetbrains.compose.resources.stringResource
import text_fields.CommonTextField
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.pages_title

@Composable
internal fun BookCreatorPagesElement(
    textState: MutableState<TextFieldValue>,
) {
    CommonTextField(
        labelText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                append(stringResource(Res.string.pages_title))
            }

            withStyle(style = SpanStyle(color = ApplicationTheme.colors.readingStatusesColor.readingStatusColor)) {
                append("*")
            }
        },
        textState = textState,
        maxLines = 1,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        isError = textState.value.text.isNotEmpty() && textState.value.text.toIntOrNull() == null,
        imeAction = ImeAction.Done
    )
}