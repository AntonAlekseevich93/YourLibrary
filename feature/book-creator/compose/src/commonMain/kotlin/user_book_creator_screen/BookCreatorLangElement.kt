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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import main_models.books.LANG

@Composable
internal fun BookCreatorLangElement(
    selectedLang: State<LANG>,
    selectedLangListener: (LANG) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 12.dp),
    ) {
        itemsIndexed(LANG.entries) { index, lang ->
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
                modifier = Modifier.padding(start = if (index == 0) 16.dp else 0.dp),
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