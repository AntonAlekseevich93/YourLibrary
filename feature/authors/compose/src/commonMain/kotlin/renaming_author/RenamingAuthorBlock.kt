package renaming_author

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import text_fields.OldCommonTextField

@Composable
fun RenamingAuthorBlock(
    textField: State<TextFieldValue>,
    onTextChanged: (textFieldValue: TextFieldValue) -> Unit,
) {
    Card(
        border = BorderStroke(1.dp, ApplicationTheme.colors.divider),
        backgroundColor = ApplicationTheme.colors.textFieldColorDarkOld,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        OldCommonTextField(
            modifier = Modifier.sizeIn(maxWidth = 140.dp, minWidth = 140.dp),
            textState = textField.value,
            onTextChanged = onTextChanged,
        )
    }
}