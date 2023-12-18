package text_fields

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.utf16CodePoint
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import text_fields.RussianKeys.Companion.KEY_A
import text_fields.RussianKeys.Companion.KEY_C
import text_fields.RussianKeys.Companion.KEY_COMMAND
import text_fields.RussianKeys.Companion.KEY_V
import text_fields.RussianKeys.Companion.KEY_X

@Composable
fun InputTextField(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    hint: String = "",
    minHeight: Dp = 30.dp,
    contentTextFieldPadding: PaddingValues? = null,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    isFocusedListener: ((isFocus: Boolean) -> Unit)? = null,
    onMessageSent: () -> Unit,
) {
    var ctrlPressed by remember { mutableStateOf(false) }
    var commandPressed by remember { mutableStateOf(false) }
    var lastFocusState by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        MyTextField(
            value = textFieldValue.text,
            onValueChange = {
                if (!ctrlPressed) onTextChanged(textFieldValue.copy(it))
            },
            placeholder = {
                Text(
                    text = hint,
                    fontSize = 14.sp,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainPlaceholderTextColor
                )
            },
            colors = colors,
            modifier = Modifier
                .defaultMinSize(minHeight = minHeight)
                .fillMaxWidth()
                .heightIn(max = 100.dp)
                .onFocusChanged { state ->
                    isFocusedListener?.invoke(state.isFocused)
                    if (lastFocusState != state.isFocused) {
                        onTextFieldFocused(state.isFocused)
                    }
                    lastFocusState = state.isFocused
                }
                .onKeyEvent {
                    when (it.utf16CodePoint) {
                        KEY_COMMAND -> {
                            when (it.type) {
                                KeyEventType.KeyDown -> {
                                    commandPressed = true
                                }

                                KeyEventType.KeyUp -> {
                                    commandPressed = false
                                }
                            }
                        }
                    }
                    //copy
                    if (it.utf16CodePoint == KEY_C && commandPressed && it.type == KeyEventType.KeyDown) {
                        clipboardManager.setText(textFieldValue.getSelectedText())
                        return@onKeyEvent true
                    }
                    //insert
                    if (it.utf16CodePoint == KEY_V && commandPressed && it.type == KeyEventType.KeyDown) {
                        onTextChanged(getTextToInsert(textFieldValue, clipboardManager))
                        return@onKeyEvent true
                    }
                    //full selection
                    if (it.utf16CodePoint == KEY_A && commandPressed && it.type == KeyEventType.KeyDown) {
                        val textLength = textFieldValue.text.length
                        if (textLength > 0) {
                            onTextChanged(
                                textFieldValue.copy(
                                    text = textFieldValue.text,
                                    selection = TextRange(0, textLength)
                                )
                            )
                        }
                        return@onKeyEvent true
                    }
                    //cut
                    if (it.utf16CodePoint == KEY_X && commandPressed && it.type == KeyEventType.KeyDown) {
                        val selectionText = textFieldValue.getSelectedText()
                        clipboardManager.setText(selectionText)
                        val selectionRange = textFieldValue.selection
                        if (selectionRange.end == textFieldValue.text.length) {
                            onTextChanged(
                                textFieldValue.copy(
                                    text = "",
                                    selection = TextRange(0, 0)
                                )
                            )
                        } else {
                            val firstPart =
                                textFieldValue.text.substring(0, selectionRange.start)
                            val secondPart = textFieldValue.text.substring(
                                selectionRange.end,
                                textFieldValue.text.length
                            )
                            onTextChanged(
                                textFieldValue.copy(
                                    text = firstPart + secondPart,
                                    selection = TextRange(firstPart.length, firstPart.length)
                                )
                            )
                        }
                        return@onKeyEvent true
                    }

//                        println("Info ${it.utf16CodePoint} type = ${it.type} key = ${it.key}")

                    when (it.key) {
                        Key.Enter -> {
                            if (it.type == KeyEventType.KeyDown && ctrlPressed) {
                                if (isMessageIdCorrect(textFieldValue.text)) {
                                    onMessageSent()
                                }
                                true
                            } else false
                        }

                        Key.CtrlLeft,
                        Key.CtrlRight,
                        -> {
                            when (it.type) {
                                KeyEventType.KeyDown -> {
                                    ctrlPressed = true
                                    true
                                }

                                KeyEventType.KeyUp -> {
                                    ctrlPressed = false
                                    true
                                }

                                else -> false
                            }
                        }

                        else -> false
                    }
                },

            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Send
            ),
            maxLines = 100,
            contentPadding = contentTextFieldPadding ?: PaddingValues(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 5.dp
            )
        )
    }
}


private fun isMessageIdCorrect(message: String): Boolean =
    message.isNotBlank() && message.isNotEmpty() && message.length >= MIN_LENGTH_MESSAGE

private fun getTextToInsert(
    textFieldValue: TextFieldValue,
    clipboardManager: ClipboardManager,
): TextFieldValue {
    val cursorIsEnd =
        textFieldValue.selection.end >= textFieldValue.text.length
    val clipboardText = clipboardManager.getText()
    val clipboardLength = clipboardText?.length ?: 0

    val newText = if (cursorIsEnd) {
        textFieldValue.text + clipboardManager.getText()
    } else {
        val cursorPosition = textFieldValue.selection.end
        val firstPart = textFieldValue.text.substring(0, cursorPosition)
        val secondPart = textFieldValue.text.substring(
            cursorPosition,
            textFieldValue.text.length
        )
        firstPart + clipboardText + secondPart
    }

    return textFieldValue.copy(
        text = newText,
        selection = if (cursorIsEnd) TextRange(
            newText.length,
            newText.length
        ) else {
            TextRange(
                textFieldValue.selection.end + clipboardLength,
                textFieldValue.selection.end + clipboardLength
            )
        }
    )
}

private const val MIN_LENGTH_MESSAGE = 2


enum class InputSelector {
    NONE,
    PICTURE
}

class RussianKeys {
    companion object {
        const val KEY_V = 118
        const val KEY_C = 99
        const val KEY_A = 97
        const val KEY_X = 120
        const val KEY_COMMAND = 65535
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.TextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentPadding: PaddingValues = PaddingValues(
        start = 10.dp,
        end = 10.dp,
        top = 5.dp,
        bottom = 5.dp
    )
) {

    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    @OptIn(ExperimentalMaterialApi::class)
    BasicTextField(
        value = value,
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .indicatorLine(enabled, isError, interactionSource, colors),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = contentPadding
            )
        }
    )
}