package text_fields

import ApplicationTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.utf16CodePoint
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import text_fields.RussianKeys.Companion.KEY_A
import text_fields.RussianKeys.Companion.KEY_C
import text_fields.RussianKeys.Companion.KEY_COMMAND
import text_fields.RussianKeys.Companion.KEY_V
import text_fields.RussianKeys.Companion.KEY_X

@Deprecated("Remove this. This is from another app")
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
    enabled: Boolean = true,
    isFocusedListener: ((isFocus: Boolean) -> Unit)? = null,
    onMessageSent: () -> Unit,
) {
    var ctrlPressed by remember { mutableStateOf(false) }
    var commandPressed by remember { mutableStateOf(false) }
    var lastFocusState by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicCustomTextField(
            textFieldValue = textFieldValue,
            onValueChange = {
                if (!ctrlPressed) onTextChanged(it)
            },
            placeholder = {
                Text(
                    text = hint,
                    fontSize = 14.sp,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainPlaceholderTextColor
                )
            },
            enabled = enabled,
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

