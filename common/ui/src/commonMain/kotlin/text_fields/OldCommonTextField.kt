package text_fields

import ApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OldCommonTextField(
    textState: TextFieldValue,
    modifier: Modifier = Modifier,
    onTextChanged: (TextFieldValue) -> Unit,
    enabled: Boolean = true,
    maxLines: Int = 1,
    shape: Shape = RoundedCornerShape(4.dp),
    focusedIndicatorLineThickness: Dp = 0.dp,
    unfocusedIndicatorLineThickness: Dp = 0.dp,
    placeholder: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = ApplicationTheme.colors.mainTextColor,
        disabledTextColor = ApplicationTheme.colors.mainPlaceholderTextColor,
    ),
    textStyle: TextStyle = LocalTextStyle.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    contentPadding: PaddingValues = PaddingValues(
        start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp
    ),
    label: @Composable (() -> Unit)? = null,
) {
    BasicCustomTextField(
        textFieldValue = textState,
        onValueChange = { onTextChanged(it) },
        colors = colors,
        modifier = modifier,
        contentPadding = contentPadding,
        maxLines = maxLines,
        textStyle = textStyle,
        enabled = enabled,
        placeholder = placeholder,
        singleLine = maxLines <= 1,
        shape = shape,
        focusedIndicatorLineThickness = focusedIndicatorLineThickness,
        unfocusedIndicatorLineThickness = unfocusedIndicatorLineThickness,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        label = label
    )
}