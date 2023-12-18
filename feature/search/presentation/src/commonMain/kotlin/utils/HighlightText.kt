package utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

internal fun String.toHighlightText(
    searchText: String,
    style: SpanStyle = SpanStyle(),
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle()) {
            var searchTextStartIndex = indexOf(searchText, ignoreCase = true)
            var searchTextEndIndex = searchTextStartIndex + searchText.length
            var currentTextStartIndex = 0

            while (searchTextStartIndex >= 0) {
                // Add non-matching text
                append(this@toHighlightText.substring(currentTextStartIndex, searchTextStartIndex))

                // Add matching text
                withStyle(style) {
                    append(this@toHighlightText.substring(searchTextStartIndex, searchTextEndIndex))
                }

                // Move startIndex to end of the matching text
                currentTextStartIndex = searchTextEndIndex
                searchTextStartIndex = indexOf(searchText, currentTextStartIndex, ignoreCase = true)
                searchTextEndIndex = searchTextStartIndex + searchText.length
            }

            // Add any remaining non-matching text
            append(
                this@toHighlightText.substring(
                    currentTextStartIndex,
                    this@toHighlightText.length
                )
            )
        }
    }
}