package date

import ApplicationTheme
import DateUtils
import Strings
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component_colors.getDatePickerAppColor
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDatePicker(
    title: String = Strings.date_utils_title,
    state: DatePickerState,
    onSelectedListener: (millis: Long, text: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val text =
        mutableStateOf(
            if (state.selectedDateMillis != null) {
                DateUtils.getDateInStringFromMillis(
                    state.selectedDateMillis!!, Locale.ROOT
                )
            } else {
                Strings.date_utils_headline
            }
        )

    DatePickerDialog(
        onDismissRequest = {
            onDismissRequest.invoke()
        },
        confirmButton = {
            TextButton(onClick = {
                if (state.selectedDateMillis != null) {
                    onSelectedListener.invoke(state.selectedDateMillis ?: 0, text.value)
                }
                onDismissRequest.invoke()
            }, modifier = Modifier.padding(end = 16.dp)) {
                Text(
                    text = Strings.select,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            }
        },
        colors = getDatePickerAppColor()
    ) {
        DatePicker(
            state = state,
            dateFormatter = DatePickerFormatter(
                DateUtils.DATE_FORMAT,
                DateUtils.DATE_FORMAT,
                DateUtils.DATE_FORMAT
            ),
            colors = getDatePickerAppColor(),
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            },
            headline = {
                Text(text.value, modifier = Modifier.padding(start = 16.dp))
            }
        )
    }
}