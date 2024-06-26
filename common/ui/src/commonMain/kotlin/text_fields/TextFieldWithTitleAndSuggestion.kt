package text_fields

import ApplicationTheme
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import platform.Platform
import platform.isDesktop
import tags.CustomTag

const val DELAY_FOR_LISTENER_PROCESSING = 170L

@Composable
fun TextFieldWithTitleAndSuggestion(
    platform: Platform,
    title: String,
    titleColor: Color = ApplicationTheme.colors.mainTextColor,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    hintText: String = "",
    setAsSelected: Boolean = false,
    enabledInput: Boolean = true,
    freezeFocusWhenOnClick: Boolean = false,
    maxLines: Int = 10,
    disableSingleLineIfFocused: Boolean = false,
    maxLinesIfDisableSingleLineWhenFocused: Int = 5,
    suggestionList: State<List<String>> = mutableStateOf(emptyList()),
    maxWidthSuggestions: Boolean = false,
    disableHiddenSuggestion: Boolean = false,
    disableBorder: Boolean = false,
    textFieldValue: MutableState<TextFieldValue>? = null,
    text: MutableState<String>? = null,
    showClearButton: MutableState<Boolean> = mutableStateOf(false),
    showSuggestionAsTag: Boolean = false,
    tagColor: Color = Color.White,
    onTextChanged: ((TextFieldValue) -> Unit)? = null,
    onSuggestionClickListener: ((item: String) -> Unit)? = null,
    onClearButtonListener: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    suggestionMaxHeight: Dp = 320.dp,
    hiddenText: String? = null,
    customIsFocused: MutableState<Boolean>? = null,
    topContent: @Composable ((isFocused: Boolean) -> Unit)? = null,
    innerContent: @Composable ((isFocused: Boolean) -> Unit)? = null,
    bottomContent: @Composable ((isFocused: Boolean) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val textState = remember { mutableStateOf(TextFieldValue()) }
    var isFocused = customIsFocused ?: remember { mutableStateOf(false) }
    val maxLinesResult: MutableState<Int> = remember {
        mutableStateOf(
            maxLines
        )
    }

    if (disableSingleLineIfFocused) {
        LaunchedEffect(isFocused.value) {
            if (isFocused.value) {
                maxLinesResult.value = maxLinesIfDisableSingleLineWhenFocused
            } else {
                maxLinesResult.value = 1
            }
        }
    }

    Column {
        topContent?.invoke(isFocused.value)

        Card(
            modifier = modifier
                .fillMaxWidth()
                .hoverable(
                    interactionSource = interactionSource,
                    enabled = true,
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
            border = if (!disableBorder && (setAsSelected || isHovered.value || isFocused.value)) BorderStroke(
                if (isFocused.value || setAsSelected) 2.dp else if (isHovered.value) 1.dp else 0.dp,
                color = ApplicationTheme.colors.textFieldColor
            ) else null
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ApplicationTheme.colors.mainIconsColor,
                        modifier = Modifier.padding(end = 4.dp, start = 4.dp).size(18.dp)
                    )
                }

                Text(
                    text = title,
                    modifier = Modifier.padding(end = 2.dp, start = 4.dp)
                        .sizeIn(minWidth = if (platform.isDesktop()) 80.dp else 80.dp),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = titleColor,
                )
                if (enabledInput) {
                    Box {
                        CommonTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { isFocused.value = it.isFocused },
                            textState = textFieldValue?.value ?: textState.value,
                            onTextChanged = {
                                if (textFieldValue == null) {
                                    textState.value = it
                                }
                                onTextChanged?.invoke(it)
                            },
                            enabled = enabledInput,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = ApplicationTheme.colors.mainTextColor,
                                disabledTextColor = ApplicationTheme.colors.mainTextColor,
                                backgroundColor = if (!disableBorder && (setAsSelected || isHovered.value || isFocused.value))
                                    ApplicationTheme.colors.focusedTextFillBackground
                                else ApplicationTheme.colors.cardBackgroundDark,
                            ),
                            placeholder = {
                                Text(
                                    text = hintText,
                                    style = ApplicationTheme.typography.footnoteRegular,
                                    color = ApplicationTheme.colors.hintColor
                                )
                            },
                            maxLines = maxLinesResult.value,
                            textStyle = ApplicationTheme.typography.bodyRegular,
                        )
                        innerContent?.invoke(isFocused.value)
                        if (showClearButton.value) {
                            ClearButton(onClearButtonListener)
                        }
                    }
                } else {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (!disableBorder && (setAsSelected || isHovered.value || isFocused.value))
                                ApplicationTheme.colors.focusedTextFillBackground
                            else ApplicationTheme.colors.cardBackgroundDark
                        ).clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onClick?.invoke()
                            if (freezeFocusWhenOnClick) {
                                isFocused.value = true
                            }
                        }
                    ) {
                        val textModifier = Modifier.padding(
                            start = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp,
                            end = 16.dp
                        )
                        if (
                            hiddenText != null ||
                            (textFieldValue?.value?.text == null &&
                                    text?.value?.isEmpty() == true
                                    && textState.value.text.isEmpty() ||
                                    textFieldValue?.value?.text?.isEmpty() == true
                                    )
                        ) {
                            Text(
                                text = hiddenText ?: hintText,
                                modifier = textModifier,
                                style = ApplicationTheme.typography.footnoteRegular,
                                color = ApplicationTheme.colors.hintColor
                            )
                        }

                        if (hiddenText == null && !showSuggestionAsTag) {
                            Text(
                                text = textFieldValue?.value?.text ?: text?.value
                                ?: textState.value.text,
                                modifier = textModifier,
                                style = ApplicationTheme.typography.bodyRegular,
                                color = ApplicationTheme.colors.mainTextColor,
                            )
                        } else if (hiddenText == null) {
                            CustomTag(
                                text = textFieldValue?.value?.text ?: text?.value
                                ?: textState.value.text,
                                color = tagColor,
                                modifier = textModifier,
                                onClick = { isFocused.value = true }
                            )
                        }

                        if (showClearButton.value) {
                            ClearButton(onClearButtonListener)
                        }
                    }
                }
            }
        }

        bottomContent?.invoke(isFocused.value)

        AnimatedVisibility(
            visible = suggestionList.value.isNotEmpty() && isFocused.value
                    || disableHiddenSuggestion && suggestionList.value.isNotEmpty(),
            /**the delay is necessary because otherwise the listener does not have time to work out**/
            exit = fadeOut(animationSpec = tween(1, DELAY_FOR_LISTENER_PROCESSING.toInt()))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 4.dp,
                        start = if (maxWidthSuggestions) 0.dp else if (platform.isDesktop()) 136.dp else 116.dp
                    )
                    .sizeIn(maxHeight = suggestionMaxHeight),
                contentAlignment = Alignment.Center
            ) {
                DropdownSuggestionList(
                    list = suggestionList.value,
                    itemClickListener = {
                        onSuggestionClickListener?.invoke(it)
                        isFocused.value = false
                    },
                )
            }
        }
    }
}

@Composable
internal fun BoxScope.ClearButton(
    onClearButtonListener: (() -> Unit)?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .matchParentSize(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Card(
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    onClearButtonListener?.invoke()
                },
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.mainBackgroundColor.copy(
                    0.5f
                )
            )
        ) {
            Text(
                text = Strings.clear,
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColor,
            )
        }
    }
}

@Composable
fun DropdownSuggestionList(
    list: List<String>,
    modifier: Modifier = Modifier,
    itemClickListener: (item: String) -> Unit,
) {

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ApplicationTheme.colors.dropdownBackground
        ),
        modifier = modifier

    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            items(list) {
                DropdownSuggestionItem(
                    text = it,
                    itemClickListener = itemClickListener,
                )
            }
        }
    }
}

@Composable
fun DropdownSuggestionItem(
    text: String,
    itemClickListener: (item: String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    Card(
        modifier = Modifier
            .padding(bottom = 2.dp)
            .fillMaxWidth()
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ).clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                itemClickListener.invoke(text)
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHovered.value) ApplicationTheme.colors.mainBackgroundColor else
                ApplicationTheme.colors.dropdownBackground
        ),

        ) {
        Text(
            text = text,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 12.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}