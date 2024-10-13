package text

import ApplicationTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.collapse
import yourlibrary.common.resources.generated.resources.expand

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    collapsedMaxLine: Int = 3,
    showMoreText: String = stringResource(Res.string.expand),
    showMoreStyle: SpanStyle = SpanStyle(color = Color(0xFFedf6f9), fontWeight = FontWeight.Bold),
    showLessText: String = stringResource(Res.string.collapse),
    showLessStyle: SpanStyle = showMoreStyle,
    style: TextStyle = ApplicationTheme.typography.headlineRegular,
    color: Color = ApplicationTheme.colors.mainTextColor,
    textAlign: TextAlign? = null,
    disableOnClick: Boolean = false,
    showMoreOrShowLessAsNewLine: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            clickable
        ) {
            if (!disableOnClick) {
                isExpanded = !isExpanded
            }
            onClick?.invoke()
        }
        .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        append("... ")
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        val adjustText = text.substring(
                            startIndex = 0,
                            endIndex = minOf(lastCharIndex, text.length)
                        )
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        append("... ")
                        if (showMoreOrShowLessAsNewLine) {
                            append("\n")
                        }
                        withStyle(style = showMoreStyle) { append(showMoreText) }
                    }
                } else {
                    append(text)
                }
            },
            color = color,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = textAlign
        )
    }
}