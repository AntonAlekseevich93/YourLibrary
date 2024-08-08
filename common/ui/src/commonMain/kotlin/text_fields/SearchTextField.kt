package text_fields

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import utils.debounceClick

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    textFieldValue: MutableState<TextFieldValue>? = null,
    onTextChanged: ((TextFieldValue) -> Unit)? = null,
    hintText: String = "",
    iconResName: DrawableResource? = null,
    onClickSearch: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val textState = remember { mutableStateOf(TextFieldValue()) }
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
        border = if (isHovered.value || isFocused) BorderStroke(
            if (isFocused) 2.dp else if (isHovered.value) 1.dp else 0.dp,
            color = ApplicationTheme.colors.textFieldColor
        ) else null
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            iconResName?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp).size(18.dp)
                )
            }


            CommonTextField(
                modifier = Modifier
                    .sizeIn(minHeight = 50.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textState = textFieldValue?.value ?: textState.value,
                onTextChanged = {
                    if (textFieldValue == null) {
                        textState.value = it
                    }
                    onTextChanged?.invoke(it)
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = ApplicationTheme.colors.mainTextColor,
                    disabledTextColor = ApplicationTheme.colors.mainTextColor,
                    backgroundColor = if (isHovered.value || isFocused)
                        ApplicationTheme.colors.focusedTextFillBackground
                    else ApplicationTheme.colors.cardBackgroundDark,
                ),
                placeholder = {
                    Text(
                        text = hintText,
                        style = ApplicationTheme.typography.bodyRegular,
                        color = ApplicationTheme.colors.hintColor
                    )
                },
                maxLines = 1,
                textStyle = ApplicationTheme.typography.bodyRegular,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onClickSearch.invoke() }),
            )

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp).size(18.dp).debounceClick(
                    interactionSource, null, onClick = { onClickSearch() }
                )
            )
        }
    }
}