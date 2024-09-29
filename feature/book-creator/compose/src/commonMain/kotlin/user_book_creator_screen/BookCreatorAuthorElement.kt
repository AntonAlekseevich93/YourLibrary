package user_book_creator_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import main_models.AuthorVo
import models.BookCreatorEvents
import org.jetbrains.compose.resources.stringResource
import text_fields.CommonTextField
import user_book_creator_screen.elements.ExactMachAuthorTooltipIcon
import user_book_creator_screen.elements.TextFieldWasLockedTooltipIcon
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.autor
import yourlibrary.common.resources.generated.resources.lock_author_field_tooltip

@Composable
internal fun BaseEventScope<BaseEvent>.BookCreatorAuthorElement(
    isEnabled: Boolean,
    textState: MutableState<TextFieldValue>,
    showSearchAuthorLoader: State<Boolean>,
    exactMatchSearchedAuthor: State<AuthorVo?>,
    oldTypedAuthorNameText: MutableState<String>,
) {
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        oldTypedAuthorNameText.value = textState.value.text
    }

    CommonTextField(
        labelText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                append(stringResource(Res.string.autor))
            }

            withStyle(style = SpanStyle(color = ApplicationTheme.colors.readingStatusesColor.readingStatusColor)) {
                append("*")
            }
        },
        textState = textState,
        textChangedListener = {
            if (isFocused && oldTypedAuthorNameText.value != it && (showSearchAuthorLoader.value || exactMatchSearchedAuthor.value != null)) {
                sendEvent(BookCreatorEvents.CancelUserBookSearchAuthor)
            }
        },
        maxLines = 1,
        enabled = isEnabled,
        modifier = Modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (isEnabled && !focusState.isFocused && oldTypedAuthorNameText.value != textState.value.text) {
                    oldTypedAuthorNameText.value = textState.value.text
                    sendEvent(BookCreatorEvents.StartUserBookSearchAuthor(textState.value.text))
                }
            }
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
        imeAction = ImeAction.Done,
        trailingIcon = {
            if (!isEnabled) {
                TextFieldWasLockedTooltipIcon(
                    tooltipText = stringResource(Res.string.lock_author_field_tooltip)
                )
            } else if (exactMatchSearchedAuthor.value != null) {
                ExactMachAuthorTooltipIcon()
            } else
                if (showSearchAuthorLoader.value) {
                    CircularProgressIndicator(
                        color = ApplicationTheme.colors.screenColor.activeButtonColor,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(28.dp).padding(end = 4.dp)
                    )
                }
        }
    )
}