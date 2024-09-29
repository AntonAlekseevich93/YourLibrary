package text_fields

import ApplicationTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.None,
    isError: Boolean = false,
    textFieldColors: TextFieldColors = getCommonTextFieldColors(),
    textChangedListener: ((changedText: String) -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textState: MutableState<TextFieldValue>? = null,
) {
    var textStateResult by remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        value = textState?.value ?: textStateResult,
        onValueChange = {
            if (textState?.value != null) {
                textState.value = it
            } else {
                textStateResult = it
            }
            textChangedListener?.invoke(it.text)
        },
        label = {
            label?.let {
                Text(text = it)
            }
        },
        singleLine = maxLines == 1,
        maxLines = maxLines,
        enabled = enabled,
        colors = textFieldColors,
        textStyle = ApplicationTheme.typography.headlineRegular,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
    )
}

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    labelText: AnnotatedString? = null,
    enabled: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.None,
    isError: Boolean = false,
    textFieldColors: TextFieldColors = getCommonTextFieldColors(),
    textChangedListener: ((changedText: String) -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textState: MutableState<TextFieldValue>? = null,
) {
    var textStateResult by remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        value = textState?.value ?: textStateResult,
        onValueChange = {
            if (textState?.value != null) {
                textState.value = it
            } else {
                textStateResult = it
            }
            textChangedListener?.invoke(it.text)
        },
        label = {
            labelText?.let {
                Text(text = it)
            }
        },
        maxLines = maxLines,
        singleLine = maxLines == 1,
        enabled = enabled,
        colors = textFieldColors,
        textStyle = ApplicationTheme.typography.headlineRegular,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
    )
}

@Composable
fun getCommonTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
    focusedLabelColor = ApplicationTheme.colors.mainTextColor,
    disabledLabelColor = ApplicationTheme.colors.mainTextColor,
    unfocusedLabelColor = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
    errorLabelColor = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
    cursorColor = ApplicationTheme.colors.textFieldColor.cursorColor,
    errorCursorColor = ApplicationTheme.colors.textFieldColor.cursorColor,
    focusedIndicatorColor = ApplicationTheme.colors.textFieldColor.focusedIndicatorColor,
    unfocusedIndicatorColor = ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
    disabledIndicatorColor = ApplicationTheme.colors.textFieldColor.disabledIndicatorColor,
    errorIndicatorColor = ApplicationTheme.colors.textFieldColor.errorIndicatorColor,
    disabledTextColor = ApplicationTheme.colors.mainTextColor,
    focusedTextColor = ApplicationTheme.colors.mainTextColor,
    unfocusedTextColor = ApplicationTheme.colors.mainTextColor,
    errorTextColor = ApplicationTheme.colors.mainTextColor,
)

