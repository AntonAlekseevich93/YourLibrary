package user_book_creator_screen

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import main_models.books.LANG
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.book_language

@Composable
internal fun BookCreatorLangElement(
    selectedLang: State<LANG>,
    selectedLangListener: (LANG) -> Unit,
) {
    val lastIndex = remember { LANG.entries.size - 1 }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(LANG.entries) { index, lang ->
            if (index == 0) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                            append(stringResource(Res.string.book_language))
                        }
                    },
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            val isSelected = lang == selectedLang.value
            FilterChip(
                selected = isSelected,
                onClick = { selectedLangListener(lang) },
                label = {
                    Text(
                        lang.translatedName,
                        style = ApplicationTheme.typography.bodyBold,
                        color = if (isSelected) ApplicationTheme.colors.screenColor.selectedTitleTextColor
                        else ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
                        textAlign = TextAlign.Center,
                    )
                },
                modifier = Modifier.padding(
                    start = if (index == 0) 16.dp else 0.dp,
                    end = if (lastIndex == index) 16.dp else 0.dp
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Transparent,
                    selectedContainerColor = Color.Transparent
                ),
                border = BorderStroke(
                    if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
                )
            )
        }
    }
}