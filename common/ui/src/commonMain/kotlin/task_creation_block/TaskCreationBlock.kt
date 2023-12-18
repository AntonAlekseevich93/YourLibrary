package task_creation_block

import ApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import text_fields.InputSelector
import text_fields.InputTextField

@Composable
fun TaskCreationBlock(
    modifier: Modifier = Modifier,
    contentTextFieldPadding: PaddingValues? = null,
    shape: Shape = CardDefaults.shape,
    hint: String = "",
    minHeight: Dp = 30.dp,
) {
    Column(modifier = modifier) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = shape
        ) {
            InputBlock(hint, minHeight, contentTextFieldPadding = contentTextFieldPadding)
        }
    }
}

@Composable
fun InputBlock(
    hint: String,
    minHeight: Dp = 30.dp,
    contentTextFieldPadding: PaddingValues? = null,
) {
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    var textState by remember { mutableStateOf(TextFieldValue()) }
    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollStateQuoteThread = rememberLazyListState()
    val scrollState = rememberLazyListState()
    InputTextField(
        textFieldValue = textState,
        onTextChanged = { textState = it },
        // Only show the keyboard if there's no input selector and text field has focus
        // keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
        // Close extended selector if text field receives focus
        onTextFieldFocused = { focused ->
            if (focused) {
                currentInputSelector = InputSelector.NONE
                //this name reset scroll
                coroutineScope.launch {
                    scrollStateQuoteThread.scrollToItem(0)
                    scrollState.scrollToItem(0)
                }
            }
            textFieldFocusState = focused
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Red,
            backgroundColor = ApplicationTheme.colors.cardBackgroundLight,
            textColor = ApplicationTheme.colors.mainTextColor,
            focusedIndicatorColor = Color.Transparent,
        ),
        contentTextFieldPadding = contentTextFieldPadding,
        hint = hint,
        minHeight = minHeight,
    ) { }
}