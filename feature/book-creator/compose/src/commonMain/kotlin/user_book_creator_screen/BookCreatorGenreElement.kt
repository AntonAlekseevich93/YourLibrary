package user_book_creator_screen

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import main_models.genre.Genre

@Composable
internal fun BookCreatorGenreElement(
    selectedGenre: State<Genre?>,
    onClick: () -> Unit,
) {
    val genreIsSelected = remember(selectedGenre.value) {
        selectedGenre.value?.name != null
    }
    FilterChip(
        selected = false,
        onClick = onClick,
        label = {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (genreIsSelected)
                                ApplicationTheme.colors.readingStatusesColor.readingStatusColor
                            else ApplicationTheme.colors.textFieldColor.unfocusedLabelColor
                        )
                    ) {
                        if (genreIsSelected) {
                            append(selectedGenre.value?.name.orEmpty())
                        } else {
                            append("Выбрать жанр")
                        }
                    }
                    if (!genreIsSelected) {
                        withStyle(
                            style = SpanStyle(
                                color = ApplicationTheme.colors.readingStatusesColor.readingStatusColor,
                            )
                        ) {
                            append("*")
                        }
                    }
                },
                style = ApplicationTheme.typography.bodyBold,
                color = if (selectedGenre.value?.name == null) {
                    ApplicationTheme.colors.textFieldColor.unfocusedLabelColor
                } else ApplicationTheme.colors.readingStatusesColor.readingStatusColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)
            )
        },
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp).fillMaxWidth(),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            selectedContainerColor = Color.Transparent,
        ),
        border = BorderStroke(
            1.dp,
            color = ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
        )
    )
}