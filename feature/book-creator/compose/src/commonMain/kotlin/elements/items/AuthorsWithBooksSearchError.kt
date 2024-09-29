package elements.items

import ApplicationTheme
import Strings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import containters.CenterBoxContainer
import error.SearchError

@Composable
fun AuthorsWithBooksSearchError(bookName: String, authorName: String, onClickManually: () -> Unit) {
    SearchError(
        titleAnnotationString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = ApplicationTheme.colors.mainTextColor)) {
                append(Strings.search_is_empty)
                append(":\n")
            }

            withStyle(
                style = SpanStyle(
                    color = ApplicationTheme.colors.mainTextColor,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )
            ) {
                if (bookName.isNotEmpty()) {
                    append(bookName)
                    if (authorName.isNotEmpty()) {
                        append(", ")
                    }
                }

                if (authorName.isNotEmpty()) {
                    append(authorName)
                }
            }

        },
        title = null
    )
    CenterBoxContainer {
        Text(
            text = "Создать новую книгу?",
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.screenColor.activeButtonColor,
            modifier = Modifier.fillMaxWidth().padding(top = 36.dp).clickable(
                MutableInteractionSource(), null
            ) {
                onClickManually()
            },
            textAlign = TextAlign.Center
        )
    }
}