package user_book_creator_screen

import ApplicationTheme
import DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import main_models.ReadingStatus
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.date_picker_end_date_title
import yourlibrary.common.resources.generated.resources.date_picker_start_date_title
import yourlibrary.common.resources.generated.resources.hint_reading_end_date
import yourlibrary.common.resources.generated.resources.hint_reading_start_date
import java.util.Locale

@Composable
internal fun BookCreatorDateElement(
    readingStatus: State<ReadingStatus>,
    startDate: State<Long>,
    endDate: State<Long>,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
) {
    val modifier = if (readingStatus.value == ReadingStatus.DONE) Modifier.padding(8.dp) else {
        Modifier.padding(start = 8.dp, end = 8.dp, top = 18.dp, bottom = 18.dp)
    }

    if (readingStatus.value != ReadingStatus.PLANNED) {
        Row(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 8.dp)) {
            FilterChip(
                selected = false,
                onClick = onStartDateClick,
                label = {
                    val resultText = if (startDate.value > 0) {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                                append(stringResource(Res.string.date_picker_start_date_title))
                                append("\n")
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = ApplicationTheme.colors.screenColor.selectedTitleTextColor,
                                )
                            ) {
                                append(
                                    DateUtils.getDateInStringFromMillis(
                                        startDate.value,
                                        Locale.ROOT
                                    )
                                )
                            }
                        }
                    } else {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                                append(stringResource(Res.string.hint_reading_start_date))
                            }
                        }
                    }
                    Text(
                        resultText,
                        style = ApplicationTheme.typography.bodyBold,
                        color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
                        textAlign = TextAlign.Center,
                        modifier = modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Transparent,
                    selectedContainerColor = Color.Transparent
                ),
                border = BorderStroke(
                    1.dp,
                    color = ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
                )
            )
            if (readingStatus.value == ReadingStatus.DONE) {
                val isError = endDate.value > 0 && startDate.value > endDate.value
                Spacer(Modifier.padding(8.dp))
                FilterChip(
                    selected = false,
                    onClick = onEndDateClick,
                    label = {
                        val resultText = if (endDate.value > 0) {
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                                    append(stringResource(Res.string.date_picker_end_date_title))
                                    append("\n")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = ApplicationTheme.colors.screenColor.selectedTitleTextColor,
                                    )
                                ) {
                                    append(
                                        DateUtils.getDateInStringFromMillis(
                                            endDate.value,
                                            Locale.ROOT
                                        )
                                    )
                                }
                            }
                        } else {
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor)) {
                                    append(stringResource(Res.string.hint_reading_end_date))
                                }
                            }
                        }
                        Text(
                            resultText,
                            style = ApplicationTheme.typography.bodyBold,
                            color =
                            ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        selectedContainerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        1.dp,
                        color = if (isError) ApplicationTheme.colors.textFieldColor.errorIndicatorColor else ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
                    ),
                )
            }
        }
    }
}