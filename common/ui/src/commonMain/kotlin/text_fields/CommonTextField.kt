package text_fields

import ApplicationTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    labelText: String? = null,
    textChangedListener: ((changedText: String) -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var textState by remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = textState,
        onValueChange = {
            textState = it
            textChangedListener?.invoke(it.text)
        },
        label = {
            labelText?.let {
                Text(text = it)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = ApplicationTheme.colors.mainTextColor,
            cursorColor = ApplicationTheme.colors.readingStatusesColor.deferredStatusColor,
            focusedIndicatorColor = ApplicationTheme.colors.hintColor.copy(0.7f),
            unfocusedIndicatorColor = ApplicationTheme.colors.hintColor.copy(0.7f),
            disabledTextColor = ApplicationTheme.colors.mainTextColor,
            focusedTextColor = ApplicationTheme.colors.mainTextColor,
            disabledLabelColor = ApplicationTheme.colors.mainTextColor,
            unfocusedLabelColor = ApplicationTheme.colors.mainTextColor,

            ),
        textStyle = ApplicationTheme.typography.headlineRegular,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = trailingIcon,
    )
}